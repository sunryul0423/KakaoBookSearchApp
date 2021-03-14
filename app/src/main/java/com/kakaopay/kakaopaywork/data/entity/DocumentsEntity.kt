package com.kakaopay.kakaopaywork.data.entity

import com.kakaopay.kakaopaywork.data.Meta

data class SearchBookEntity(
    val meta: Meta,
    val documents: List<DocumentsEntity>
)

data class DocumentsEntity(
    val title: String?,
    val contents: String?,
    val url: String?,
    val isbn: String?,
    val datetime: String?,
    val publisher: String?,
    val price: Int,
    val salePrice: Int,
    val thumbnail: String?,
    var isHeart: Boolean = false
)
