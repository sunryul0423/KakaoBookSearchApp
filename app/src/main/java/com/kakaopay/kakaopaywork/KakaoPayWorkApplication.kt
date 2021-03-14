package com.kakaopay.kakaopaywork

import android.app.Application
import com.kakaopay.kakaopaywork.util.GlideApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KakaoPayWorkApplication : Application() {

    override fun onLowMemory() {
        super.onLowMemory()
        GlideApp.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        GlideApp.get(this).trimMemory(level)
    }
}