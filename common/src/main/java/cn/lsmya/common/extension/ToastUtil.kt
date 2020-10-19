package cn.lsmya.common.extension

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * 功能描述: 吐司工具类
 */
fun Activity.toast(
    @StringRes stringRes: Int,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, getString(stringRes), duration).apply { setGravity(gravity, 0, 0) }.show()
}

fun Activity.toast(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, msg, duration).apply { setGravity(gravity, 0, 0) }.show()
}

fun Fragment.toast(
    @StringRes stringRes: Int,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(activity, getString(stringRes), duration).apply { setGravity(gravity, 0, 0) }
        .show()
}

fun Fragment.toast(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(activity, msg, duration).apply { setGravity(gravity, 0, 0) }.show()
}