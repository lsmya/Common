package com.example.common

import android.os.Bundle
import android.util.Log
import cn.lsmya.common.BuildConfig
import cn.lsmya.common.base.BaseActivity
import cn.lsmya.common.extension.toJson
import cn.lsmya.common.extension.toast
import cn.lsmya.common.utils.loge

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading()
        "123".toJson()
        toast("123")
    }
}