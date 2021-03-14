package com.kakaopay.kakaopaywork.data.entity

import android.view.View
import java.io.Serializable

data class ClickData(val view: View, val data: DocumentsEntity, val position: Int) : Serializable
