package com.kakaopay.kakaopaywork.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kakaopay.kakaopaywork.data.entity.ClickData
import com.kakaopay.kakaopaywork.data.entity.DocumentsEntity
import com.kakaopay.kakaopaywork.domain.SearchBookUseCase
import com.kakaopay.kakaopaywork.presentation.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val handler: SavedStateHandle,
    private val useCase: SearchBookUseCase
) : BaseVM() {

    companion object {
        private const val CLICK_DATA = "clickData"
        private const val CHANGE_DATA = "changeData"
    }

    private val _searchList = MutableLiveData<PagingData<DocumentsEntity>>()
    val searchList: LiveData<PagingData<DocumentsEntity>> = _searchList
    val isListEmpty: LiveData<Boolean> = useCase.isListEmpty()
    val clickDataLive: LiveData<ClickData> = handler.getLiveData(CLICK_DATA)
    val changeDataLive: LiveData<ClickData> = handler.getLiveData(CHANGE_DATA)

    fun searchListRequest(searchTitle: String) {
        viewModelScope.launch(netWorkHandler) {
            useCase.invoke(searchTitle)
                .cachedIn(viewModelScope)
                .collect {
                    _searchList.postValue(it)
                }
        }
    }

    fun setChangeData(clickData: ClickData) {
        handler.set(CHANGE_DATA, clickData)
    }

    fun setClickData(clickData: ClickData) {
        handler[CLICK_DATA] = clickData
    }
}