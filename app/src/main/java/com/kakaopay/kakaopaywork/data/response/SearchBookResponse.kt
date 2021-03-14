package com.kakaopay.kakaopaywork.data

import com.google.gson.annotations.SerializedName
import com.kakaopay.kakaopaywork.data.entity.DocumentsEntity
import com.kakaopay.kakaopaywork.data.entity.SearchBookEntity
import com.kakaopay.kakaopaywork.util.extension.blankReplace

/**
 * 카카오 책 검색 API Response
 * @param meta 페이징관련 Object
 * @param documents 책 정보 Object
 */
data class SearchBookResponse(
    val meta: Meta,
    val documents: List<DocumentsResponse>
) {
    fun toEntity(): SearchBookEntity {
        return SearchBookEntity(
            meta = meta,
            documents = documents.map { it.toEntity() }
        )
    }
}

/**
 * @param title 도서 제목
 * @param contents 도서 소개
 * @param url 도서 상세 URL
 * @param isbn 국제 표준 도서번호
 * @param datetime 도서 출판날짜
 * @param authors 도서 저자 리스트
 * @param publisher 도서 출판사
 * @param translators 도서 번역자 리스트
 * @param price 도서 정가
 * @param salePrice 도서 판매가
 * @param thumbnail 도서 표지 미리보기 URL
 * @param status 도서 판매 상태 정보 (정상, 품절, 절판 등)
 */
data class DocumentsResponse(
    val title: String,
    val contents: String,
    val url: String,
    val isbn: String,
    val datetime: String,
    val authors: List<String>,
    val publisher: String,
    val translators: List<String>,
    val price: Int,
    @SerializedName("sale_price")
    val salePrice: Int,
    val thumbnail: String,
    val status: String
) {
    fun toEntity(): DocumentsEntity {
        return DocumentsEntity(
            title = title.blankReplace(),
            contents = contents.blankReplace(),
            url = url,
            isbn = isbn,
            datetime = datetime,
            publisher = publisher,
            price = price,
            salePrice = salePrice,
            thumbnail = thumbnail,
        )
    }
}

/**
 * @param totalCount 검색어에 검색된 문서수
 * @param pageableCount total_count 중에 노출가능 문서수
 * @param isEnd 현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음
 */
data class Meta(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)