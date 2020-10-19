package cn.lsmya.common.utils

import android.util.Log
import cn.lsmya.common.BuildConfig

fun logd(any: Any) {
    log(Log.DEBUG, any)
}

fun loge(any: Any) {
    log(Log.ERROR, any)
}

fun logi(any: Any) {
    log(Log.INFO, any)
}

fun logv(any: Any) {
    log(Log.VERBOSE, any)
}

fun logw(any: Any) {
    log(Log.WARN, any)
}

private fun log(method: Int, any: Any?) {
    if (BuildConfig.DEBUG) {
        any?.let {
            val stackTrace = Thread.currentThread().stackTrace[4]
            val tag = "log"
            val msg = String.format(
                "(%s:%d)#%s: ",
                stackTrace.fileName,
                stackTrace.lineNumber,
                stackTrace.methodName
            ) + any.toString()
            when (method) {
                Log.DEBUG -> Log.d(tag, msg)
                Log.ERROR -> Log.e(tag, msg)
                Log.INFO -> Log.i(tag, msg)
                Log.VERBOSE -> Log.v(tag, msg)
                Log.WARN -> Log.w(tag, msg)
                else -> Log.wtf(tag, msg)
            }
        }
    }

}