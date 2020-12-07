package com.example.common

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/app/TwoActivity")
class TwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
    }
    fun back(v: View){
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("data","数据数据")
        })
        finish()
    }
}