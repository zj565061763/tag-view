package com.sd.lib.tag_view

import java.util.*

interface ITagView {

    companion object {
        /** 空的View标识  */
        @JvmField
        val EMPTY_VIEW_TAG = UUID.randomUUID().toString()
    }

    /**
     * 返回View标识
     *
     * @return
     */
    val viewTag: String?

    /**
     * 查找指定类型的Item，如果不存在则返回null
     *
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T : Item> findItem(clazz: Class<T>): T?

    /**
     * 返回指定类型的Item，如果不存在则创建对象返回
     *
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T : Item> getItem(clazz: Class<T>): T

    /**
     * 销毁Item
     */
    fun destroyItem()

    /**
     * Item必须有空参数的构造方法
     */
    interface Item {
        /**
         * 初始化
         *
         * @param tagView
         */
        fun initItem(tagView: ITagView)

        /**
         * 销毁
         */
        fun destroyItem()
    }
}