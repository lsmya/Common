package cn.lsmya.common.extension

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间格式化
 */
const val ONE_DAY = 24 * 60 * 60 * 1000F

const val ONE_HOUR = 60 * 60 * 1000F


private val YYYY_FORMAT = SimpleDateFormat("yyyy", Locale.getDefault())

/**
 * 获取当前时间，并格式化为yyyy
 */

fun todayYyyy(): String {
    return YYYY_FORMAT.format(Date())
}

/**
 * 将日期格式化yyyy格式
 */

fun Date.todayYyyy(): String {
    return YYYY_FORMAT.format(this)
}

/**
 * 将时间戳格式化为yyyy格式
 */

fun Long.todayYyyy(): String {
    return YYYY_FORMAT.format(Date(this))
}


private val YYYYMMDD_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

/**
 * 获取当前时间，并格式化为yyyy-MM-dd格式
 */

fun todayYyyyMmDd(): String {
    return YYYYMMDD_FORMAT.format(Date())
}

/**
 * 将日期格式化为yyyy-MM-dd格式
 */

fun Date.todayYyyyMmDd(): String {
    return YYYYMMDD_FORMAT.format(this)
}

/**
 * 将时间戳格式化为yyyy-MM-dd格式
 */

fun Long.todayYyyyMmDd(): String {
    return YYYYMMDD_FORMAT.format(Date(this))
}


private val YYYYMMDDHHMM_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

/**
 * 获取当前时间，并格式化为yyyy-MM-dd HH:mm格式
 */

fun todayYyyyMmDdHhMm(): String {
    return YYYYMMDDHHMM_FORMAT.format(Date())
}

/**
 * 将日期格式化为yyyy-MM-dd HH:mm格式
 */

fun Date.todayYyyyMmDdHhMm(): String {
    return YYYYMMDDHHMM_FORMAT.format(this)
}

/**
 * 将时间戳格式化为yyyy-MM-dd HH:mm格式
 */

fun Long.todayYyyyMmDdHhMm(): String {
    return YYYYMMDDHHMM_FORMAT.format(Date(this))
}


private val YYYYMMDDHHMMSS_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

/**
 * 获取当前时间，并格式化为yyyy-MM-dd HH:mm:ss格式
 */

fun todayYyyyMmDdHhMmSs(): String {
    return YYYYMMDDHHMMSS_FORMAT.format(Date())
}

/**
 * 将日期格式化为yyyy-MM-dd HH:mm:ss格式
 */

fun Date.todayYyyyMmDdHhMmSs(): String {
    return YYYYMMDDHHMMSS_FORMAT.format(this)
}

/**
 * 将时间戳格式化为yyyy-MM-dd HH:mm:ss格式
 */

fun Long.todayYyyyMmDdHhMmSs(): String {
    return YYYYMMDDHHMMSS_FORMAT.format(Date(this))
}


private val HHMMSS_FORMAT = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

/**
 * 获取当前时间，并格式化为HH:mm:ss格式
 */

fun todayHhMmSs(): String {
    return HHMMSS_FORMAT.format(Date())
}

/**
 * 将日期格式化为HH:mm:ss格式
 */

fun Date.todayHhMmSs(): String {
    return HHMMSS_FORMAT.format(this)
}

/**
 * 将时间戳格式化为HH:mm:ss格式
 */

fun Long.todayHhMmSs(): String {
    return HHMMSS_FORMAT.format(Date(this))
}

/**
 * 将日期转换为时间戳
 */

fun timeToTimestamp(time: String, sdf: SimpleDateFormat): Long = sdf.parse(time)!!.time

/**
 * 计算两个日期直接的时间差
 */

fun dateCalculation(
    startTime: String,
    stopTime: String,
    sdf: SimpleDateFormat,
    unit: Float
): Float {
    var fTime: Date? = null
    var sTime: Date? = null
    try {
        fTime = sdf.parse(startTime)
        sTime = sdf.parse(stopTime)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("TIME_HELPER", "日期型字符串格式错误")
    }
    if (sTime!!.time > fTime!!.time) {
        return (sTime.time - fTime.time) / unit
    } else {
        return (fTime.time - sTime.time) / unit
    }
}

/**
 * 获取当前时间，并格式化为指定格式
 */

fun today(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
}

/**
 * 将日期格式化为指定格式
 */

fun today(date: Date, pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
}

/**
 * 将时间戳格式化为指定格式
 */

fun today(time: Long, pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(time)
}