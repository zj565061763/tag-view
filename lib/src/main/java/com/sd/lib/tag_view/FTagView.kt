package com.sd.lib.tag_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.sd.lib.tag_view.ITagView.ItemManager
import com.sd.lib.tag_view.Utils.getObjectId
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

class FTagView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs), ITagView {

    override val viewTag: String = getObjectId(this)
    override val itemManager: ItemManager by lazy { InternalItemManager() }

    private inner class InternalItemManager : ItemManager {
        private val nItemHolder: MutableMap<Class<out ITagView.Item>, ITagView.Item> = ConcurrentHashMap()

        override fun <T : ITagView.Item> findItem(clazz: Class<T>): T? {
            checkItemClass(clazz)
            val item = nItemHolder[clazz] ?: null
            return item as T
        }

        override fun <T : ITagView.Item> getItem(clazz: Class<T>): T {
            checkItemClass(clazz)
            synchronized(this@InternalItemManager) {
                val cacheItem = nItemHolder[clazz]
                if (cacheItem != null) {
                    return cacheItem as T
                }

                val item = createItem(clazz)
                nItemHolder[clazz] = item
                item.init(this@FTagView)
                return item
            }
        }

        override fun destroyItem() {
            synchronized(this@InternalItemManager) {
                for (item in nItemHolder.values) {
                    item.destroy()
                }
            }
        }

        private fun <T : ITagView.Item> createItem(clazz: Class<T>): T {
            return try {
                clazz.newInstance()
            } catch (e: Exception) {
                throw RuntimeException("Create instance failed for class:" + clazz.name, e)
            }
        }
    }

    companion object {
        private fun <T : ITagView.Item> checkItemClass(clazz: Class<T>?) {
            requireNotNull(clazz) { "clazz is null" }
            require(!clazz.isInterface) { "clazz is interface $clazz" }
            require(!Modifier.isAbstract(clazz.modifiers)) { "clazz is abstract $clazz" }
        }
    }
}