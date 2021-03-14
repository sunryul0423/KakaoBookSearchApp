package com.kakaopay.kakaopaywork.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.kakaopay.kakaopaywork.data.entity.DocumentsEntity
import com.kakaopay.kakaopaywork.util.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

interface SearchBookUseCase {
    suspend operator fun invoke(
        query: String,
        size: Int = PAGE_SIZE
    ): Flow<PagingData<DocumentsEntity>>

    fun isListEmpty(): LiveData<Boolean>
}

class SearchBookUseCaseImp(
    private val searchBookRepository: SearchBookRepository
) : SearchBookUseCase {

    override suspend operator fun invoke(
        query: String,
        size: Int
    ): Flow<PagingData<DocumentsEntity>> = searchBookRepository.getSearchBook(query, size)

    override fun isListEmpty(): LiveData<Boolean> = searchBookRepository.isListEmpty()
}