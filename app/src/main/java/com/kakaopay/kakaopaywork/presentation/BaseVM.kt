package com.kakaopay.kakaopaywork.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaopay.kakaopaywork.data.response.ErrorResponse
import com.kakaopay.kakaopaywork.util.NETWORK_ERROR
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException

open class BaseVM : ViewModel() {

    private val _exception = MutableLiveData<ErrorResponse>()
    val exception: LiveData<ErrorResponse> = _exception

    protected val netWorkHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            val errorResponse: ErrorResponse = when (throwable) {
                is HttpException -> {
                    ErrorResponse(throwable.code().toString(), NETWORK_ERROR)
                }
                else -> {
                    ErrorResponse(null, NETWORK_ERROR)
                }
            }
            _exception.postValue(errorResponse)
        }
    }
}