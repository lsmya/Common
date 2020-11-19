package com.example.common

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import cn.lsmya.common.base.SimpleActivity
import cn.lsmya.common.dialog.DialogUtil
import cn.lsmya.common.empty.SysErrModel
import cn.lsmya.common.extension.toJson
import cn.lsmya.common.extension.toast

class MainActivity : SimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading()
//        DialogUtil.newBuilder(this)
//            .setContentView(R.layout.dialog)
//            .setText(R.id.text1,"1234")
//            .setText(R.id.text2,"abcd")
//            .setText(R.id.btn1,"setTextForButton1")
//            .setText(R.id.btn2,"setTextForButton2")
//
//            .setTextColorRes(R.id.text1,R.color.colorAccent)
//            .setTextColor(R.id.text2,"#FF0000")
//            .setTextColor(R.id.btn1,Color.BLUE)
//            .build().show()
        Dialog(this).apply { setContentView(R.layout.dialog) }.show()
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