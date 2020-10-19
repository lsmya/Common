package cn.lsmya.common.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Glide加载图片
 * @param url 可以是任何Glide支持的类型
 * @param placeholder 默认占位图
 * @param error 失败占位图
 * @param options 自定义加载属性
 * @param isOriginal 是否使用原图，默认false
 * @param isCircle 是否是圆形，默认false
 * @param radius 是否是圆形，默认false，注意：isCircle和roundRadius两个只能有一个生效
 */
fun ImageView.load(
    url: Any,
    placeholder: Int = 0,
    error: Int = 0,
    options: RequestOptions? = null,
    isOriginal: Boolean = false,
    isCircle: Boolean = false,
    radius: Int = 0
) {
    Glide.with(context).load(url)
        .apply {
            if (options != null) {
                apply(options)
            } else {
                if (error != 0) {
                    error(error)
                }
                if (placeholder != 0) {
                    placeholder(placeholder)
                }
            }
            if (isOriginal) {
                override(Target.SIZE_ORIGINAL)
            }
            if (isCircle) {
                transform(CircleCrop())
            } else {
                if (radius != 0) {
                    transform(RoundedCorners(radius.dp2px().toInt()))
                }
            }
        }.into(this)
}