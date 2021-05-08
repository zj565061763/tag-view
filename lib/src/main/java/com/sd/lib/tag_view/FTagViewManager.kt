package com.sd.lib.tag_view

import android.view.View
import java.util.concurrent.ConcurrentHashMap

object FTagViewManager {
    private val _mapFinder: MutableMap<Class<out ITagView>, TagViewFinder> = ConcurrentHashMap()

    /** 是否调试模式 */
    var isDebug = false

    /**
     * 查找[view]所依附的[clazz]
     */
    fun <T : ITagView> findTagView(clazz: Class<T>, view: View): T? {
        val cachedFinder = _mapFinder[clazz]
        if (cachedFinder != null) {
            val tagView = cachedFinder.findTagView(view) ?: return null
            return tagView as T
        }

        synchronized(_mapFinder) {
            val finder = TagViewFinder(clazz, isDebug)
            _mapFinder[clazz] = finder

            val tagView = finder.findTagView(view) ?: return null
            return tagView as T
        }
    }
}