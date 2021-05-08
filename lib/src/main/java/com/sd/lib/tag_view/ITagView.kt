package com.sd.lib.tag_view

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
    }
}