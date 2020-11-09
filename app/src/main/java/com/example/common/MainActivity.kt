package com.example.common

import android.os.Bundle
import cn.lsmya.common.base.BaseActivity
import cn.lsmya.common.extension.toJson
import cn.lsmya.common.extension.toast

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading()
        "123".toJson()
        toast("123")
    }
}