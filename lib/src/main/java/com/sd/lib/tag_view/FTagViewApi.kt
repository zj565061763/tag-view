package com.sd.lib.tag_view

import android.view.View

class FTagViewApi(view: View) {
    private val mView: View = view

    fun findTagView(): ITagView? {
        return FTagViewManager.findTagView(mView)
    }

    /**
     * 准备调用当前对象的api
     *
     * @param callback
     */
    fun prepare(callback: PrepareCallback) {
        val tagView = findTagView()
        if (tagView != null) {
            callback.onPrepared(tagView)
        } else {
            mView.post {
                callback.onPrepared(findTagView()!!)
            }
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
     * [ITagView.findItem]
     */
    fun <T : ITagView.Item> findItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.findItem(clazz)
    }

    /**
     *  [ITagView.getItem]
     */
    fun <T : ITagView.Item> getItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.getItem(clazz)
    }

    /**
     * [ITagView.destroyItem]
     */
    fun destroyItem() {
        val tagView = findTagView() ?: return;
        tagView.destroyItem()
    }

    fun interface PrepareCallback {
        fun onPrepared(tagViewApi: ITagView)
    }
}