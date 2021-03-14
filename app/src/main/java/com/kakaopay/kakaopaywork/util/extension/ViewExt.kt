package com.kakaopay.kakaopaywork.util.extension

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.CheckResult
import androidx.databinding.BindingAdapter
import com.kakaopay.kakaopaywork.R
import com.kakaopay.kakaopaywork.util.GlideApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

/**
 * 키보드 내리기
 */
fun View.hideKeyboard(context: Context) {
    clearFocus()
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 키보드 올리기
 */
fun View.showKeyboard(context: Context) {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

/**
 * Glide 이미지 세팅
 */
@BindingAdapter("imgUrl")
fun ImageView.setImageUrl(url: String?) {
    GlideApp.with(context.applicationContext)
        .load(url)
        .override(width, height)
        .placeholder(R.color.charcoal)
        .error(R.color.charcoal)
        .into(this)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        offer(Unit)
    }
    awaitClose { setOnClickListener(null) }
}