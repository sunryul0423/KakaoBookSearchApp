package com.kakaopay.kakaopaywork.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.kakaopay.kakaopaywork.R
import com.kakaopay.kakaopaywork.databinding.FragmentSearchBinding
import com.kakaopay.kakaopaywork.presentation.BaseFragment
import com.kakaopay.kakaopaywork.presentation.ui.adapter.SearchBookListAdapter
import com.kakaopay.kakaopaywork.util.CommonFun
import com.kakaopay.kakaopaywork.util.extension.hideKeyboard
import com.kakaopay.kakaopaywork.util.extension.showShortToast
import com.kakaopay.kakaopaywork.util.extension.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_search

    private val mainVM: MainVM by activityViewModels()
    private var searchBookListAdapter: SearchBookListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (0 < childFragmentManager.backStackEntryCount) {
                childFragmentManager.popBackStack()
            } else {
                activity?.finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.mainVM = mainVM
        setView()
        liveDataObserver()
        return view
    }

    private fun setView() {
        searchBookListAdapter = SearchBookListAdapter { clickData ->
            binding.root.hideKeyboard(mContext)
            mainVM.setClickData(clickData)

            // SearchDetailFragment 로 이동
            childFragmentManager.beginTransaction()
                .addSharedElement(clickData.view, clickData.view.transitionName)
                .addToBackStack(null)
                .replace(binding.clFragment.id, SearchDetailFragment::class.java, null)
                .commitAllowingStateLoss()
        }
        binding.rvSearch.adapter = searchBookListAdapter

        binding.etSearchTitle.setOnEditorActionListener { view, _, _ ->
            view.clearFocus()
            return@setOnEditorActionListener false
        }

        @OptIn(FlowPreview::class)
        binding.etSearchTitle.textChanges()
            .filter { !it.isNullOrEmpty() }
            .debounce(1000)
            .onEach {
                binding.root.hideKeyboard(mContext)
                mainVM.searchListRequest(it.toString())
            }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            searchBookListAdapter?.loadStateFlow?.collectLatest { loadStates ->
                setLoadStateAction(loadStates.refresh, "refresh")
                setLoadStateAction(loadStates.append, "append")
            }
        }
    }

    /**
     * 초기화, 추가 상태 UI 처리
     */
    private fun setLoadStateAction(loadState: LoadState, str: String) {
        when (loadState) {
            is LoadState.Error -> {
                val error = loadState.error
                val errorResponse = CommonFun.createException(error)
                mContext.showShortToast(errorResponse.resultMsg)
            }
            is LoadState.Loading -> {
                Log.d("srpark", "$str Loading")
                binding.pbLoading.isVisible = true
            }
            is LoadState.NotLoading -> {
                Log.d("srpark", "$str NotLoading")
                binding.pbLoading.isVisible = false
            }
        }
    }

    private fun liveDataObserver() {
        mainVM.exception.observe(viewLifecycleOwner, {
            mContext.showShortToast(it.resultMsg)
        })

        mainVM.searchList.observe(viewLifecycleOwner, { pageData ->
            searchBookListAdapter?.submitData(lifecycle, pageData)
        })

        mainVM.changeDataLive.observe(viewLifecycleOwner, {
            searchBookListAdapter?.setItemChanged(it.position)
        })
    }
}