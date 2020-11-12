package com.example.common

import android.os.Bundle
import cn.lsmya.common.base.SimpleActivity
import cn.lsmya.common.empty.SysErrModel
import cn.lsmya.common.extension.toJson
import cn.lsmya.common.extension.toast

class MainActivity : SimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading()
        "123".toJson()
        toast("123")
    }

    override fun onEventListener(sysErrModel: SysErrModel) {
        when (sysErrModel.code) {
            else -> {
                sysErrModel.sysInfo?.let {
                    toast(it)
                }

            }

        }

    }
}