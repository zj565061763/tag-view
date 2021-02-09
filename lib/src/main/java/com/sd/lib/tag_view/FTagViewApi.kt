package com.sd.lib.tag_view

import android.view.View
import com.sd.lib.tag_view.FTagViewApi
import com.sd.lib.tag_view.Utils.isAttached

class FTagViewApi(view: View?) {
    private val mView: View

    init {
        requireNotNull(view) { "view is null" }
        mView = view
    }

    private fun findTagView(): ITagView? {
        return FTagViewManager.default.findTagView(mView)
    }

    /**
     * 是否已经准备好
     *
     * @return
     */
    val isPrepared: Boolean get() = isAttached(mView)

    /**
     * 准备调用当前对象的api
     *
     * @param callback
     */
    fun prepare(callback: PrepareCallback) {
        if (isPrepared) {
            callback.onPrepared(this@FTagViewApi)
        } else {
            mView.post { prepare(callback) }
        }
    }

    /**
     * [ITagView.viewTag]
     *
     * @return
     */
    val viewTag: String?
        get() {
            val tagView = findTagView() ?: return ITagView.EMPTY_VIEW_TAG
            return tagView.viewTag
        }

    /**
     * [ITagView.ItemManager.findItem]
     */
    fun <T : ITagView.Item> findItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.itemManager.findItem(clazz)
    }

    /**
     *  [ITagView.ItemManager.getItem]
     */
    fun <T : ITagView.Item> getItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.itemManager.getItem(clazz)
    }

    /**
     * [ITagView.ItemManager.destroyItem]
     */
    fun destroyItem() {
        val tagView = findTagView() ?: return;
        tagView.itemManager.destroyItem()
    }

    interface PrepareCallback {
        fun onPrepared(tagViewApi: FTagViewApi)
    }
}