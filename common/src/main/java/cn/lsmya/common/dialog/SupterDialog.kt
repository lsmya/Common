package cn.lsmya.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import cn.lsmya.common.R
import cn.lsmya.common.extension.getScreenWidth
import cn.lsmya.common.extension.singleClick

object SuperDialog {

    fun newBuilder(context: Context): Builder {
        return Builder(context)
    }

    class Builder(private val context: Context) {
        private var contentView: View? = null
        private var contentLayoutId = -1
        private var mGravity = Gravity.CENTER
        private var mCanceledOnTouchOutside = true
        private var mWidth = 1f
        private val themeId = R.style.common_dialog_bottom_dialog_custom
        private var animId = R.style.common_dialog_bottom_menu_animation

        private var mClickArray = SparseArray<(dialog: Dialog) -> Unit>()
        private var textString = HashMap<Int, String>()
        private var textColor = HashMap<Int, Int>()
        private var textColorRes = HashMap<Int, Int>()
        private var textColorStr = HashMap<Int, Int>()
        private var imageRes = HashMap<Int, Int>()
        private var mOnDismissListener: (() -> Unit)? = null
        private var mOnCancelListener: (() -> Unit)? = null

        /**
         * 设置显示的布局
         * @param contentView 布局view
         */
        fun setContentView(contentView: View): Builder {
            this.contentView = contentView
            return this
        }

        /**
         * 设置显示的布局
         * @param contentLayoutId 布局id
         */
        fun setContentView(contentLayoutId: Int): Builder {
            this.contentLayoutId = contentLayoutId
            return this
        }

        fun setCanceledOnTouchOutside(cancel: Boolean): Builder {
            this.mCanceledOnTouchOutside = cancel
            return this
        }

        /**
         * 设置dialog关闭\取消（dismiss\cancel）监听
         */
        fun setOnDismissCancelListener(onDismissCancelListener: (() -> Unit)?): Builder {
            this.mOnDismissListener = onDismissCancelListener
            this.mOnCancelListener = onDismissCancelListener
            return this
        }

        /**
         * 设置dialog关闭（dismiss）监听
         */
        fun setOnDismissListener(onDismissListener: (() -> Unit)?): Builder {
            this.mOnDismissListener = onDismissListener
            return this
        }

        /**
         * 设置dialog取消（cancel）监听
         */
        fun setOnCancelListener(onCancelListener: (() -> Unit)?): Builder {
            this.mOnCancelListener = onCancelListener
            return this
        }

        /**
         * 设置文字颜色
         */
        fun setTextColorRes(@IdRes id: Int, @ColorRes color: Int): Builder {
            textColorRes[id] = color
            return this
        }

        fun setTextColor(@IdRes id: Int, @ColorInt color: Int): Builder {
            textColor[id] = color
            return this
        }

        fun setTextColor(@IdRes id: Int, @Size(min = 1) color: String): Builder {
            textColorStr[id] = Color.parseColor(color)
            return this
        }

        /**
         * 设置布局内View的文字
         */
        fun setText(@IdRes id: Int, @StringRes strId: Int): Builder {
            textString[id] = context.getString(strId)
            return this
        }

        fun setText(@IdRes id: Int, text: String?): Builder {
            textString[id] = text ?: ""
            return this
        }

        /**
         * 设置布局内ImageView的image
         */
        fun setImageResource(@IdRes id: Int, @DrawableRes resId: Int): Builder {
            imageRes[id] = resId
            return this
        }

        /**
         * 设置弹窗动画
         * @param anim 动画资源id
         */
        fun setAnim(anim: Int): Builder {
            animId = anim
            return this
        }

        /**
         * 宽度充满屏幕
         */
        fun fullWidth(): Builder {
            mWidth = 1f
            return this
        }

        /**
         * 设置宽度
         * @param width 0f <= width <= 1f
         */
        fun setWidth(width: Float): Builder {
            mWidth = if (width > 1) 1f else width
            return this
        }

        /**
         * 设置点击事件
         * @param viewId 点击的view
         * @param listener 点击事件监听回调
         */
        fun setClick(viewId: Int, listener: ((dialog: Dialog) -> Unit)): Builder {
            mClickArray.put(viewId, listener)
            return this
        }

        /**
         * 设置dialog位于屏幕底部并自下向上弹出
         */
        fun fromBottom(): Builder = fromBottom(true)

        /**
         * 设置dialog位于屏幕底部并设置是否自下向上弹出
         * @param isAnimation 是否自下向上弹出
         */
        fun fromBottom(isAnimation: Boolean): Builder {
            if (isAnimation) {
                animId = R.style.common_dialog_bottom_menu_animation
            }
            mGravity = Gravity.BOTTOM
            return this
        }

        /**
         * 创建dialog，最后需要调用 show 方法
         */
        fun build(): Dialog = Dialog(context, themeId).apply {
            if (contentView != null) {
                setContentView(contentView!!)
            } else {
                setContentView(contentLayoutId)
            }
            setCanceledOnTouchOutside(mCanceledOnTouchOutside)
            val window = window
            window!!.setGravity(mGravity)
            window.setWindowAnimations(animId)
            val lp = window.attributes
            lp.width = (context.getScreenWidth() * mWidth).toInt()
            window.attributes = lp
            for (i in 0 until mClickArray.size()) {
                findViewById<View>(mClickArray.keyAt(i))
                    .singleClick {
                        mClickArray.valueAt(i).invoke(this)
                    }
            }
            for (entry in textString) {
                findViewById<TextView>(entry.key).text = entry.value
            }
            for (entry in textColorRes) {
                findViewById<TextView>(entry.key)
                    .setTextColor(ContextCompat.getColor(context, entry.value))
            }
            for (entry in textColorStr) {
                findViewById<TextView>(entry.key).setTextColor(entry.value)
            }
            for (entry in textColor) {
                findViewById<TextView>(entry.key).setTextColor(entry.value)
            }
            for (entry in imageRes) {
                findViewById<ImageView>(entry.key).setImageResource(entry.value)
            }
            setOnCancelListener { mOnCancelListener?.invoke() }
            setOnDismissListener { mOnDismissListener?.invoke() }
        }

        /**
         * 创建dialog并弹出啊
         */
        fun buildShow(): Dialog {
            val dialog = build()
            dialog.show()
            return dialog
        }

    }

}