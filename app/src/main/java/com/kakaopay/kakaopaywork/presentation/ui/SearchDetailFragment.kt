package com.kakaopay.kakaopaywork.presentation.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import com.kakaopay.kakaopaywork.R
import com.kakaopay.kakaopaywork.databinding.FragmentSearchDetailBinding
import com.kakaopay.kakaopaywork.presentation.BaseFragment
import com.kakaopay.kakaopaywork.util.extension.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample

@AndroidEntryPoint
class SearchDetailFragment : BaseFragment<FragmentSearchDetailBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_search_detail

    private val mainVM: MainVM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = ChangeBounds()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.mainVM = mainVM

        mainVM.clickDataLive.observe(viewLifecycleOwner, { clickData ->
            binding.item = clickData.data

            if (clickData.data.salePrice <= 0) {
                binding.tvPrice.run {
                    paintFlags = 0
                    setTextColor(ContextCompat.getColor(binding.root.context, R.color.teal_700))
                    textSize = 20f
                }
                binding.tvSalePrice.isVisible = false
            } else {
                binding.tvPrice.run {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(binding.root.context, R.color.charcoal))
                    textSize = 16f
                }
                binding.tvSalePrice.isVisible = true
            }

            binding.btnLike.setOnCheckedChangeListener { _, isChecked ->
                clickData.data.isHeart = isChecked
                mainVM.setChangeData(clickData)
            }
        })
        setView()
        return view
    }

    private fun setView() {
        @OptIn(FlowPreview::class)
        binding.clBackBtn.clicks()
            .sample(1000)
            .onEach { activity?.onBackPressed() }
            .launchIn(lifecycleScope)
    }
}