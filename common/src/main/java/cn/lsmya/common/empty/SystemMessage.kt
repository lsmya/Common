package cn.lsmya.common.empty

/**
 * 系统级推送事件模型
 */
data class SystemMessage(
    val code: Int = -1,
    val url: String? = "",
    val sysInfo: String? = "",
    val throwable: Throwable? = null
)