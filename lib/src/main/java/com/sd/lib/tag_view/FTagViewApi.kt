package com.sd.lib.tag_view

import android.view.View

class FTagViewApi(view: View) {
    private val _view: View = view

    /**
     * 查找[FTagView]
     */
    fun findTagView(): FTagView? {
        return FTagViewManager.findTagView(FTagView::class.java, _view)
    }

    /**
     * 准备调用当前对象的api
     */
    fun prepare(callback: PrepareCallback) {
        val tagView = findTagView()
        if (tagView != null) {
            callback.onPrepared(tagView)
        } else {
            _view.post {
                findTagView()?.let {
                    callback.onPrepared(it)
                }
            }
        }
    }

    /**
     * [FTagView.viewTag]
     */
    val viewTag: String
        get() {
            val tagView = findTagView() ?: return ITagView.EMPTY_VIEW_TAG
            return tagView.viewTag
        }

    /**
     * [FTagView.findItem]
     */
    fun <T : FTagView.Item> findItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.findItem(clazz)
    }

    /**
     *  [FTagView.getItem]
     */
    fun <T : FTagView.Item> getItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.getItem(clazz)
    }

    /**
     * [FTagView.removeItem]
     */
    fun <T : FTagView.Item> removeItem(clazz: Class<T>): T? {
        val tagView = findTagView() ?: return null
        return tagView.removeItem(clazz)
    }

    /**
     * [FTagView.destroyItem]
     */
    fun destroyItem() {
        val tagView = findTagView() ?: return;
        tagView.destroyItem()
    }

    fun interface PrepareCallback {
        fun onPrepared(tagView: FTagView)
    }
}