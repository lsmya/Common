package cn.lsmya.common.base

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class CacheFragment : Fragment() {
    protected var mViewCreated = false
    private var mCacheViews: SparseArray<View>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewCreated = true
        return getLayoutView(inflater, container)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mCacheViews != null) {
            mCacheViews!!.clear()
            mCacheViews = null
        }
        mViewCreated = false
    }

    protected abstract fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View
}
