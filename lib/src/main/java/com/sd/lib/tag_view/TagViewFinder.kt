package com.sd.lib.tag_view

import android.util.Log
import android.view.View
import java.util.concurrent.ConcurrentHashMap

internal abstract class TagViewFinder {
    /** 要查找的目标类 */
    private val _targetClass: Class<out ITagView>

    /** 缓存View树 */
    private val _mapViewCache = ConcurrentHashMap<View, ITagView>()

    /** 是否调试模式 */
    private var _isDebug: Boolean = false

    /** 缓存大小 */
    val cacheSize: Int get() = _mapViewCache.size

    constructor(targetClass: Class<out ITagView>, isDebug: Boolean) {
        _targetClass = targetClass
        _isDebug = isDebug
    }

    /**
     * 查找[view]所依附的[ITagView]
     */
    fun findTagView(view: View): ITagView? {
        if (!Utils.isAttached(view)) {
            return null
        }

        if (_isDebug) {
            Log.i(
                TagViewFinder::class.java.simpleName,
                "findTagView view:${Utils.getObjectId(view)} ----------> finder:${Utils.getHashString(this@TagViewFinder)}"
            )
        }

        var tagView = checkTagView(view)
        if (tagView != null) {
            if (_isDebug) {
                Log.i(
                    TagViewFinder::class.java.simpleName,
                    "findTagView view:${Utils.getObjectId(view)} tagView:${Utils.getObjectId(tagView)} finder:${Utils.getHashString(this@TagViewFinder)}"
                )
            }
            return tagView
        }

        val listChild = mutableListOf(view)

        var viewParent = view.parent
        while (viewParent is View) {
            val current = viewParent as View
            if (!Utils.isAttached(current)) {
                return null
            }

            tagView = checkTagView(current)
            if (tagView == null) {
                listChild.add(current)
                viewParent = current.parent;
                continue
            }

            cacheView(tagView, listChild)
            if (_isDebug) {
                Log.i(
                    TagViewFinder::class.java.simpleName, """findTagView view:${Utils.getObjectId(view)} tagView:${Utils.getObjectId(tagView)} finder:${Utils.getHashString(this@TagViewFinder)}
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
                    view.addOnAttachStateChangeListener(_onAttachStateChangeListener)
                }
            }
        }
    }

    private val _onAttachStateChangeListener: View.OnAttachStateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {}

        override fun onViewDetachedFromWindow(v: View) {
            v.removeOnAttachStateChangeListener(this)
            synchronized(this@TagViewFinder) {
                _mapViewCache.remove(v)
                if (_mapViewCache.isEmpty()) {
                    if (_isDebug) {
                        Log.i(TagViewFinder::class.java.simpleName, "view cache empty finder:${Utils.getHashString(this@TagViewFinder)}")
                    }
                    onCacheEmpty()
                }
            }
        }
    }

    protected abstract fun onCacheEmpty()
}