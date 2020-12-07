package com.example.common

import android.content.Intent
import android.os.Bundle
import cn.lsmya.common.base.SimpleActivity
import cn.lsmya.common.empty.SysErrModel
import cn.lsmya.common.extension.toast
import cn.lsmya.common.utils.loge

class MainActivity : SimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.rootView,TestFragment()).commit()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loge("+++++++!!!!!!!!!!!!!!!")
    }
}