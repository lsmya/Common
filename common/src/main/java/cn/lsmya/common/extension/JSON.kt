package cn.lsmya.common.extension

import com.google.gson.Gson
import java.lang.reflect.Type

fun Any.toJson() = Gson().toJson(this)

fun <T> String.fromJson(classOfT: Class<T>): T = Gson().fromJson<T>(this, classOfT)
fun <T> String.fromJson(type: Type): T = Gson().fromJson<T>(this, type)
