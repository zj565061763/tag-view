package com.sd.lib.tag_view

import android.util.Log
import android.view.View
import com.sd.lib.tag_view.Utils.getObjectId
import com.sd.lib.tag_view.Utils.isAttached
import java.util.concurrent.ConcurrentHashMap

class FTagViewManager {

    companion object {
        @JvmStatic
        val default: FTagViewManager by lazy { FTagViewManager() }
    }

    private val mMapViewCache: MutableMap<View, ITagView> = ConcurrentHashMap()
    var mIsDebug = false

    /**
     * 查找[ITagView]
     *
     * @param view
     * @return
     */
    fun findTagView(view: View): ITagView? {
        if (!isAttached(view)) {
            return null
        }

        if (mIsDebug) {
            Log.i(FTagViewManager::class.java.simpleName, "findTagView view:${getObjectId(view)} ---------->")
        }

        var tagView = checkTagView(view)
        if (tagView != null) {
            if (mIsDebug) {
                Log.i(FTagViewManager::class.java.simpleName, "findTagView view:${getObjectId(view)} tagView:${getObjectId(tagView)}")
            }
            return tagView
        }

        val listChild = mutableListOf(view)

        var viewParent = view.parent
        while (viewParent is View) {
            val parent = viewParent as View
            if (!isAttached(parent)) {
                return null
            }

            tagView = checkTagView(parent)
            if (tagView == null) {
                listChild.add(parent)
                viewParent = parent.parent;
                continue
            }

            cacheView(tagView, listChild)
            if (mIsDebug) {
                Log.i(FTagViewManager::class.java.simpleName, """findTagView view:${getObjectId(view)} tagView:${getObjectId(tagView)}
                    | level:${listChild.size}
                    | cacheSize:${mMapViewCache.size}
                """.trimMargin()
                )
            }
            return tagView
        }

        throw RuntimeException(ITagView::class.java.simpleName + " was not found int view tree " + view)
    }

    private fun checkTagView(view: View): ITagView? {
        return if (view is ITagView) view else mMapViewCache[view]
    }

    /**
     * 缓存View
     *
     * @param tagView
     * @param list
     */
    private fun cacheView(tagView: ITagView, list: List<View>) {
        synchronized(this@FTagViewManager) {
            for (view in list) {
                if (!isAttached(view)) {
                    continue
                }

                val put = mMapViewCache.put(view, tagView)
                if (put == null) {
                    view.addOnAttachStateChangeListener(mOnAttachStateChangeListener)
                }
            }
        }
    }

    private val mOnAttachStateChangeListener: View.OnAttachStateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {}

        override fun onViewDetachedFromWindow(v: View) {
            v.removeOnAttachStateChangeListener(this)
            synchronized(this@FTagViewManager) {
                mMapViewCache.remove(v)
            }
        }
    }
}