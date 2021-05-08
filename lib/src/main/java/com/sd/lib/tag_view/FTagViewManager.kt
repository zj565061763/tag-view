package com.sd.lib.tag_view

import android.util.Log
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
            val finder = object : TagViewFinder(clazz, isDebug) {
                override fun onCacheEmpty() {
                    removeFinder(clazz)
                }
            }

            val tagView = finder.findTagView(view) ?: return null
            if (finder.cacheSize > 0) {
                _mapFinder[clazz] = finder
                if (isDebug) {
                    Log.i(FTagViewManager::class.java.simpleName, "save finder class:${clazz.name} finder:${Utils.getHashString(finder)} size:${_mapFinder.size}")
                }
            }
            return tagView as T
        }
    }

    /**
     * 移除[TagViewFinder]
     */
    private fun removeFinder(clazz: Class<out ITagView>) {
        synchronized(_mapFinder) {
            _mapFinder.remove(clazz)

            if (isDebug) {
                Log.i(FTagViewManager::class.java.simpleName, "remove finder class:${clazz.name} size:${_mapFinder.size}")
            }
        }
    }
}