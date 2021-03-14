package com.kakaopay.kakaopaywork.util.extension

import android.content.Context
import android.widget.Toast

infix fun Context.showShortToast(toastString: String) {
    Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show()
}