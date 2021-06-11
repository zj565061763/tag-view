package com.sd.lib.tag_view

import android.view.View
import java.util.*

interface ITagView {
    /**
     * 返回View标识
     */
    val viewTag: String?

    companion object {
        /** 空的View标识  */
        @JvmField
        val EMPTY_VIEW_TAG = UUID.randomUUID().toString()

        /**
         * 查找[view]所依附的[T]
         */
        inline fun <reified T : ITagView> find(view: View): T? {
            val clazz = T::class.java
            return FTagViewManager.findTagView(clazz, view)
        }

        /**
         * 查找[view]所依附的[T]的[viewTag]
         */
        inline fun <reified T : ITagView> findTag(view: View): String? {
            val clazz = T::class.java
            return FTagViewManager.findTag(clazz, view)
        }
    }
}