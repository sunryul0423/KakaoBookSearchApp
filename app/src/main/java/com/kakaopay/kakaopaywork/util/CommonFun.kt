package com.kakaopay.kakaopaywork.util

import com.kakaopay.kakaopaywork.data.response.ErrorResponse
import retrofit2.HttpException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


object CommonFun {

    private const val YYYY_MM_DD_T_HH_MM_SS_SSSZ: String = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    private const val YYYY_MM_DD: String = "yyyy-MM-dd"

    /**
     * 화페단위(원) 적용
     * @param number 숫자
     * @return ex)45,000원
     */
    fun transDecimalWon(number: Any): String {
        return DecimalFormat("#,###").format(number).plus("원")
    }

    /**
     * ISO8601 타입 yyyy-MM-dd'T'HH:mm:ss.SSSZ 변경
     * @param dateTime ISO8601 타입
     * @return yyyy-MM-dd HH:mm (ex:2019-02-21)
     */
    fun strDateTime(dateTime: String?): String? {
        return if (dateTime.isNullOrEmpty()) {
            null
        } else {
            val date = SimpleDateFormat(
                YYYY_MM_DD_T_HH_MM_SS_SSSZ,
                Locale.getDefault()
            ).parse(dateTime)
            if (date == null) {
                null
            } else {
                SimpleDateFormat(
                    YYYY_MM_DD,
                    Locale.getDefault()
                ).format(date)
            }
        }
    }

    fun createException(throwable: Throwable): ErrorResponse {
        return when (throwable) {
            is HttpException -> {
                ErrorResponse(throwable.code().toString(), NETWORK_ERROR)
            }
            else -> {
                ErrorResponse(null, NETWORK_ERROR)
            }
        }
    }
}