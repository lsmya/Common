package cn.lsmya.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import cn.lsmya.common.R
import cn.lsmya.common.extension.getScreenWidth

object DialogUtil {
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
        private var mClickArray = SparseArray<OnViewClickListener>()
        private var textString = HashMap<Int, String>()
        private var textColor = HashMap<Int, Int>()
        private var textColorRes = HashMap<Int, Int>()
        private var textColorStr = HashMap<Int, Int>()
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

        fun setOnCancelListener(onCancelListener: (() -> Unit)?): Builder {
            this.mOnCancelListener = onCancelListener
            return this
        }

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

        fun setText(@IdRes id: Int, @StringRes strId: Int): Builder {
            textString[id] = context.getString(strId)
            return this
        }

        fun setText(@IdRes id: Int, text: String?): Builder {
            textString[id] = text ?: ""
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
        fun setClick(viewId: Int, listener: OnViewClickListener): Builder {
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
        fun build(): Dialog {
            val dialog = Dialog(context, themeId)
            if (contentLayoutId == -1) {
                dialog.setContentView(contentView!!)
            } else {
                dialog.setContentView(contentLayoutId)
            }
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
            val window = dialog.window
            window!!.setGravity(mGravity)
            window.setWindowAnimations(animId)
            val lp = window.attributes
            lp.width = (context.getScreenWidth() * mWidth).toInt()
            window.attributes = lp
            for (i in 0 until mClickArray.size()) {
                dialog.findViewById<View>(mClickArray.keyAt(i))
                    .setOnClickListener { mClickArray.valueAt(i).onClick(dialog) }
            }
            for (entry in textString) {
                dialog.findViewById<TextView>(entry.key).text = entry.value
            }
            for (entry in textColorRes) {
                dialog.findViewById<TextView>(entry.key)
                    .setTextColor(ContextCompat.getColor(context, entry.value))
            }
            for (entry in textColorStr) {
                dialog.findViewById<TextView>(entry.key).setTextColor(entry.value)
            }
            for (entry in textColor) {
                dialog.findViewById<TextView>(entry.key).setTextColor(entry.value)
            }
            dialog.setOnCancelListener { mOnCancelListener?.invoke() }
            return dialog
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

    interface OnViewClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param dialog Dialog
         */
        fun onClick(dialog: Dialog)
    }
}