package com.kakaopay.kakaopaywork.domain

import com.kakaopay.kakaopaywork.data.SearchBookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val SEARCH_IMAGE: String = "v3/search/book?target=title"
    }

    @GET(SEARCH_IMAGE)
    suspend fun getBookSearch(
        @Query("query") searchTitle: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): SearchBookResponse
}