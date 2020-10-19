package cn.lsmya.common.empty

/**
 * 系统级错误推送事件模型
 */
data class SysErrModel(
    val code: Int = -1,
    val url: String = "",
    val sysInfo: String? = "未知错误",
    val throwable: Throwable? = null
)