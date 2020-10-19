package cn.lsmya.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import cn.lsmya.common.R
import cn.lsmya.common.extension.getScreenWidth

/**
 * 全屏弹窗
 */
open class DialogFullScreen(
    context: Context,
    themeId:Int = R.style.common_dialog_bottom_dialog_custom
) :
    Dialog(context, themeId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lp = window?.attributes
        lp?.width = context.getScreenWidth()
        window?.attributes = lp

    }

    override fun dismiss() {
        val view = currentFocus
        if (view is TextView) {
            val mInputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
        super.dismiss()
    }
}