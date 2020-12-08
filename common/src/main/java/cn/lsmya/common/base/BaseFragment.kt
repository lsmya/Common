package cn.lsmya.common.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.lsmya.common.R
import cn.lsmya.common.empty.SysErrModel
import cn.lsmya.common.extension.registerEventBus
import cn.lsmya.common.extension.unregisterEventBus
import cn.lsmya.common.extension.view
import org.greenrobot.eventbus.Subscribe

abstract class BaseFragment : CacheFragment() {
    private var mIsFirstVisible = true
    private var isSupportUserVisible = false
    private var loadingDialog: Dialog? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (mViewCreated) {
            if (isVisibleToUser && !isSupportUserVisible) {
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && isSupportUserVisible) {
                dispatchUserVisibleHint(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (enableEventBus()) {
            registerEventBus()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!isHidden && userVisibleHint) {
            dispatchUserVisibleHint(true)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisible) {
            if (!isHidden && !isSupportUserVisible && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSupportUserVisible && userVisibleHint) {
            dispatchUserVisibleHint(false)
        }
    }

    open fun enableEventBus(): Boolean {
        return false
    }

    @Subscribe
    fun onEvent(sysErrModel: SysErrModel) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsFirstVisible = true
        if (enableEventBus()) {
            unregisterEventBus()
        }
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    private fun dispatchUserVisibleHint(visible: Boolean) {
        if (visible && !isParentVisible) {
            return
        }
        if (isSupportUserVisible == visible) {
            return
        }
        isSupportUserVisible = visible
        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                onVisible(true)
            } else {
                onVisible(false)
            }
            dispatchChildVisibleState(true)
        } else {
            dispatchChildVisibleState(false)
            onInvisible()
        }
    }

    private val isParentVisible: Boolean
        get() {
            val fragment = parentFragment ?: return true
            if (fragment is BaseFragment) {
                return fragment.isSupportUserVisible
            }
            return fragment.isVisible
        }

    private fun dispatchChildVisibleState(visible: Boolean) {
        val childFragmentManager = childFragmentManager
        val fragments =
            childFragmentManager.fragments
        if (fragments.isNotEmpty()) {
            for (child in fragments) {
                if (child is BaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    child.dispatchUserVisibleHint(visible)
                }
            }
        }
    }

    protected open fun onVisible(isFirstVisible: Boolean) {}
    protected open fun onInvisible() {}
    protected fun goActivity(cls: Class<*>?) {
        val intent = Intent(activity, cls)
        startActivity(intent)
    }

    protected fun goActivityForResult(cls: Class<*>?, requestCode: Int) {
        val intent = Intent(activity, cls)
        startActivityForResult(intent, requestCode)
    }

    fun showLoading() {
        if (loadingDialog == null) {
            val view = activity!!.view(R.layout.common_loading, null);
            loadingDialog = Dialog(activity!!, R.style.common_dialog_loading)
            loadingDialog!!.setContentView(view)
        }
        if (loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
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
    }

}
