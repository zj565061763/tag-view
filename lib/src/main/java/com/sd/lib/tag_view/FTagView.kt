package com.sd.lib.tag_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

class FTagView : FrameLayout, ITagView {

    private val mItemHolder: MutableMap<Class<out ITagView.Item>, ITagView.Item> = ConcurrentHashMap()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override val viewTag: String = Utils.getObjectId(this)

    override fun <T : ITagView.Item> findItem(clazz: Class<T>): T? {
        checkItemClass(clazz)
        val item = mItemHolder[clazz] ?: null
        return item as T
    }

    override fun <T : ITagView.Item> getItem(clazz: Class<T>): T {
        checkItemClass(clazz)
        synchronized(this@FTagView) {
            val cacheItem = mItemHolder[clazz]
            if (cacheItem != null) {
                return cacheItem as T
            }

            val item = createItem(clazz)
            mItemHolder[clazz] = item
            item.initItem(this@FTagView)
            return item
        }
    }

    override fun destroyItem() {
        synchronized(this@FTagView) {
            for (item in mItemHolder.values) {
                item.destroyItem()
            }
        }
    }

    companion object {

        @JvmStatic
        private fun <T : ITagView.Item> checkItemClass(clazz: Class<T>?) {
            requireNotNull(clazz) { "clazz is null" }
            require(!clazz.isInterface) { "clazz is interface $clazz" }
            require(!Modifier.isAbstract(clazz.modifiers)) { "clazz is abstract $clazz" }
        }

        @JvmStatic
        private fun <T : ITagView.Item> createItem(clazz: Class<T>): T {
            return try {
                clazz.newInstance()
            } catch (e: Exception) {
                throw RuntimeException("Create instance failed for class:" + clazz.name, e)
            }
        }
    }
}