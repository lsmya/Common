package cn.lsmya.common.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.lsmya.common.R
import cn.lsmya.common.empty.SysErrModel
import cn.lsmya.common.empty.SysHideLoadingModel
import cn.lsmya.common.extension.postEvent
import cn.lsmya.common.extension.registerEventBus
import cn.lsmya.common.extension.unregisterEventBus
import cn.lsmya.common.extension.view
import cn.lsmya.common.utils.navigation
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ktx.immersionBar
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class SimpleActivity : AppCompatActivity() {

    private var loadingDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        if (enableTranslucent()) {
            immersionBar {
                navigationBarColor("#00000000")
                statusBarDarkFont(enableLightMode())
                keyboardEnable(keyboardEnable())  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
            }
        }
    }

    /**
     * 用于设置页面中EventBus的注册时机和回收时机,
     * 如果页面需要在[onCreate]中初始化的时候则需要将值设置为true
     * 如仅需要当页面在可见情况加进行EventBus事件处理时,保持默认即可
     *
     * 当设置返回值为true时,应用页面生命周期内将一直会接收对应注解内容的回调事件
     */
    protected open fun isGlobleIntercepterEventbusEvent() = false

    /**
     * 是否开启亮色状态栏
     *
     * @return
     */
    protected open fun keyboardEnable(): Boolean {
        return true
    }

    /**
     * 是否开启亮色状态栏
     *
     * @return
     */
    protected open fun enableLightMode(): Boolean {
        return true
    }

    /**
     * 是否需要沉浸式
     *
     * @return
     */
    protected open fun enableTranslucent(): Boolean {
        return true
    }

    /**
     * 接收错误信息推送
     *
     * [isGlobleIntercepterEventbusEvent]如果为true的话则activity即使处于后台也能接收到，只要没有被销毁
     * 则可以一直接收推送信息
     * @param sysErrModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(sysErrModel: SysErrModel) {
        onEventListener(sysErrModel)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(sysErrModel: SysHideLoadingModel) {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
    }

    /**
     * 可以在此处处理弹出各种toast错误，当前为主线程
     *  when (sysErrModel.code) {
     *      else -> {
     *          sysErrModel.sysInfo?.let {
     *               toast(it)
     *           }
     *       }
     *  }
     */
    abstract fun onEventListener(sysErrModel: SysErrModel)

    protected fun goActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    protected fun goActivityForResult(cls: Class<*>?, requestCode: Int) {
        val intent = Intent(this, cls)
        startActivityForResult(intent, requestCode)
    }

    /**
     * 隐藏键盘
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    protected fun hideKeyBoard(): Boolean {
        val imm =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }

    /**
     * 显示软键盘
     *
     * @param activity Activity
     */
    open fun showSoftInput(activity: Activity) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    override fun onResume() {
        super.onResume()
        if (!isGlobleIntercepterEventbusEvent()) {
            registerEventBus()
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyBoard()
        if (!isGlobleIntercepterEventbusEvent()) {
            unregisterEventBus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isGlobleIntercepterEventbusEvent()) {
            unregisterEventBus()
        }
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    /**
     * 清空所有activity并打开新activity
     */
    fun clearActivity(path: String) {
        navigation(
            url = path,
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }

    protected fun addFragment(containId: Int, fragment: Class<*>) {
        val tag = fragment.simpleName
        try {
            val fragment1 = supportFragmentManager.findFragmentByTag(fragment.simpleName)
            val transaction = supportFragmentManager.beginTransaction()
            if (null == fragment1) {
                val fragments = supportFragmentManager.fragments
                if (fragments.isNotEmpty()) {
                    for (fragmentIn in fragments) {
                        if (fragmentIn.isVisible) {
                            transaction.hide(fragmentIn)
                                .add(containId, fragment.newInstance() as Fragment, tag).commit()
                            break
                        }
                    }
                } else {
                    transaction.add(containId, fragment.newInstance() as Fragment, tag).commit()
                }
            } else {
                if (fragment1.isHidden) {
                    if (fragment1.isAdded) {
                        for (fragmentIn in supportFragmentManager.fragments) {
                            if (fragmentIn.isVisible) {
                                transaction.hide(fragmentIn).show(fragment1).commit()
                                break
                            }
                        }
                    } else {
                        transaction.add(containId, fragment1, tag).commit()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 显示加载loading动画
     */
    fun showLoading() {
        if (loadingDialog == null) {
            val view = view(R.layout.common_loading, null);
            loadingDialog = Dialog(this, R.style.common_dialog_loading)
            loadingDialog!!.setContentView(view)
        }
        if (loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
            SysHideLoadingModel().postEvent()
        } else {
            loadingDialog!!.show()
        }
    }

    /**
     * 隐藏加载loading动画
     */
    fun hideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
        SysHideLoadingModel().postEvent()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyBoard()
        }
        return super.onTouchEvent(event)
    }

}