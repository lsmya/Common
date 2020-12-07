package com.example.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.lsmya.common.base.BaseFragment
import cn.lsmya.common.extension.singleClick
import cn.lsmya.common.utils.navigation
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        click.singleClick {
            navigation(activity=activity,url = "/app/TwoActivity",requestCode = 11)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("---","---------")
    }
}