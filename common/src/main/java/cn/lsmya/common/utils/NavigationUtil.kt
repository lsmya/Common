package cn.lsmya.common.utils

import android.app.Activity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * ARouter路由跳转
 */
fun navigation(
    activity: Activity? = null,
    url: String,
    requestCode: Int = -1,
    block: (Postcard.() -> Unit)? = null
) {
    val postcard = ARouter.getInstance().build(url)
    postcard?.let {
        block?.let {
            postcard.apply(it)
        }
        if (activity == null) {
            postcard.navigation()
        } else {
            if (requestCode != -1) {
                postcard.navigation(activity, requestCode)
            } else {
                postcard.navigation()
            }
        }
    }
}