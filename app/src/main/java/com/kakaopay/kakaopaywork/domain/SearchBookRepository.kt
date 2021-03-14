package com.kakaopay.kakaopaywork.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.kakaopay.kakaopaywork.data.entity.DocumentsEntity
import com.kakaopay.kakaopaywork.util.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchBookRepository {
    suspend fun getSearchBook(
        query: String,
        size: Int
    ): Flow<PagingData<DocumentsEntity>>

    fun isListEmpty(): LiveData<Boolean>
}

class SearchBookRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchBookRepository {

    private val _isEmpty = MutableLiveData<Boolean>()

    override suspend fun getSearchBook(
        query: String,
        size: Int
    ): Flow<PagingData<DocumentsEntity>> {
        val pageConfig = PagingConfig(pageSize = PAGE_SIZE)
        val pagingSource = object : PagingSource<Int, DocumentsEntity>() {
            override fun getRefreshKey(state: PagingState<Int, DocumentsEntity>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DocumentsEntity> {
                val nextPageNumber = params.key ?: 1
                try {
                    val response = apiService.getBookSearch(query, nextPageNumber, size)
                    val nextKey = if (response.toEntity().meta.isEnd) {
                        null
                    } else {
                        nextPageNumber.plus(1)
                    }

                    _isEmpty.postValue(response.toEntity().meta.totalCount == 0)
                    return LoadResult.Page(
                        data = response.toEntity().documents,
                        prevKey = null,
                        nextKey = nextKey
                    )
                } catch (e: Throwable) {
                    return LoadResult.Error(e)

                }
            }
        }

        return Pager(pageConfig, 1) {
            pagingSource
        }.flow
    }

    override fun isListEmpty(): LiveData<Boolean> = _isEmpty
}