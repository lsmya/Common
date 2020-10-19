package cn.lsmya.common.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.*

/**
 * <pre>
 * 功能描述: 协程
 * @author Administrator
 * @version V1.0
 * </pre>
 */


//异步操作
fun async(block: suspend () -> Unit): Job {
    return GlobalScope.launch {
        block.invoke()
    }
}

fun FragmentActivity.ui(block: () -> Unit) {
    runOnUiThread(block)
}

fun Fragment.ui(block: () -> Unit) {
    activity?.ui(block)
}
