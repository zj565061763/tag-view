package com.sd.lib.tag_view

import android.util.Log
import android.view.View
import java.util.concurrent.ConcurrentHashMap

internal class TagViewFinder {
    private val _targetClass: Class<out ITagView>
    private val _mapViewCache: MutableMap<View, ITagView> = ConcurrentHashMap()

    /** 是否调试模式 */
    var isDebug = false

    constructor(targetClass: Class<out ITagView>) {
        _targetClass = targetClass
    }

    /**
     * 查找[view]所依附的[ITagView]
     */
    fun findTagView(view: View): ITagView? {
        if (!Utils.isAttached(view)) {
            return null
        }

        if (isDebug) {
            Log.i(TagViewFinder::class.java.simpleName, "findTagView view:${Utils.getObjectId(view)} ---------->")
        }

        var tagView = checkTagView(view)
        if (tagView != null) {
            if (isDebug) {
                Log.i(TagViewFinder::class.java.simpleName, "findTagView view:${Utils.getObjectId(view)} tagView:${Utils.getObjectId(tagView)}")
            }
            return tagView
        }

        val listChild = mutableListOf(view)

        var viewParent = view.parent
        while (viewParent is View) {
            val parent = viewParent as View
            if (!Utils.isAttached(parent)) {
                return null
            }

            tagView = checkTagView(parent)
            if (tagView == null) {
                listChild.add(parent)
                viewParent = parent.parent;
                continue
            }

            cacheView(tagView, listChild)
            if (isDebug) {
                Log.i(
                    TagViewFinder::class.java.simpleName, """findTagView view:${Utils.getObjectId(view)} tagView:${Utils.getObjectId(tagView)}
                    | level:${listChild.size}
                    | cacheSize:${_mapViewCache.size}
                """.trimMargin()
                )
            }
            return tagView
        }

        throw RuntimeException(ITagView::class.java.simpleName + " was not found int view tree " + view)
    }

    private fun checkTagView(view: View): ITagView? {
        return if (_targetClass.isAssignableFrom(view.javaClass)) {
            view as ITagView
        } else {
            _mapViewCache[view]
        }
    }

    /**
     * 缓存View
     */
    private fun cacheView(tagView: ITagView, list: List<View>) {
        synchronized(this@TagViewFinder) {
            for (view in list) {
                if (!Utils.isAttached(view)) {
                    continue
                }

                val put = _mapViewCache.put(view, tagView)
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
            synchronized(this@TagViewFinder) {
                _mapViewCache.remove(v)

                if (isDebug) {
                    if (_mapViewCache.isEmpty()) {
                        Log.i(TagViewFinder::class.java.simpleName, "view cache empty")
                    }
                }
            }
        }
    }
}