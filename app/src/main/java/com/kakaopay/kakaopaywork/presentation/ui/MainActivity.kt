package com.kakaopay.kakaopaywork.presentation.ui

import android.os.Bundle
import com.kakaopay.kakaopaywork.R
import com.kakaopay.kakaopaywork.databinding.ActivityMainBinding
import com.kakaopay.kakaopaywork.presentation.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView() {
        supportFragmentManager.beginTransaction()
            .replace(binding.clFragment.id, SearchFragment::class.java, null)
            .commit()
    }
}
