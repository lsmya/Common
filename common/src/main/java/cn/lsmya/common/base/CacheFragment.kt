package com.sd.common.base

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment


abstract class CacheFragment : Fragment() {
    var rootView: View? = null
        protected set
    protected var mViewCreated = false
    private var mCacheViews: SparseArray<View>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            val layoutId = getLayout()
            if (layoutId > 0) {
                rootView = inflater.inflate(layoutId, container, false)
            }
        }
        mViewCreated = true
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
        if (mCacheViews != null) {
            mCacheViews!!.clear()
            mCacheViews = null
        }
        mViewCreated = false
    }

    fun <V : View> getView(@IdRes id: Int): V {
        if (mCacheViews == null) {
            mCacheViews = SparseArray()
        }
        var view = mCacheViews!![id]
        if (view == null) {
            view = findViewById(id)
            if (view != null) {
                mCacheViews!!.put(id, view)
            }
        }
        return view as V
    }

    fun <V : View?> findViewById(@IdRes id: Int): V? {
        return if (rootView == null) {
            null
        } else rootView!!.findViewById<V>(id)
    }
    @LayoutRes
    protected abstract fun getLayout(): Int
}
