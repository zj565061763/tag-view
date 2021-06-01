package com.sd.lib.tag_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FTagView : FrameLayout, ITagView {
    /** 保存[Item] */
    private val _itemHolder: MutableMap<Class<out Item>, Item> = ConcurrentHashMap()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override val viewTag: String by lazy {
        Utils.getObjectId(this@FTagView)
    }

    /**
     * 查找指定类型的Item，如果不存在则返回null
     */
    fun <T : Item> findItem(clazz: Class<T>): T? {
        checkItemClass(clazz)
        val item = _itemHolder[clazz] ?: null
        return item as T
    }

    /**
     * 返回指定类型的Item，如果不存在则创建对象返回
     */
    fun <T : Item> getItem(clazz: Class<T>): T {
        checkItemClass(clazz)
        val cacheItem = _itemHolder[clazz]
        if (cacheItem != null) {
            return cacheItem as T
        }

        synchronized(this@FTagView) {
            val item = createItem(clazz)
            _itemHolder[clazz] = item
            item.initItem(this@FTagView)
            return item
        }
    }

    /**
     * 移除并销毁Item
     */
    fun <T : Item> removeItem(clazz: Class<T>): T? {
        checkItemClass(clazz)
        synchronized(this@FTagView) {
            val cacheItem = _itemHolder.remove(clazz) ?: return null
            cacheItem.destroyItem()
            return cacheItem as T
        }
    }

    /**
     * 清空并销毁所有Item
     */
    fun destroyItem() {
        synchronized(this@FTagView) {
            for (item in _itemHolder.values) {
                item.destroyItem()
            }
            _itemHolder.clear()
        }
    }

    /**
     * Item必须有空参数的构造方法
     */
    interface Item {
        /**
         * 初始化
         */
        fun initItem(tagView: ITagView)

        /**
         * 销毁
         */
        fun destroyItem()
    }

    companion object {
        private fun <T : Item> checkItemClass(clazz: Class<T>) {
            require(!clazz.isInterface) { "clazz is interface $clazz" }
            require(!Modifier.isAbstract(clazz.modifiers)) { "clazz is abstract $clazz" }
        }

        private fun <T : Item> createItem(clazz: Class<T>): T {
            return try {
                clazz.newInstance()
            } catch (e: Exception) {
                throw RuntimeException("Create instance failed for class:" + clazz.name, e)
            }
        }
    }
}