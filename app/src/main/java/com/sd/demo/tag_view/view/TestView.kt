package com.sd.demo.tag_view.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.sd.demo.tag_view.item.TestItem
import com.sd.demo.tag_view.view.TestView
import com.sd.lib.tag_view.FTagViewApi

class TestView : FrameLayout {
    private val mTagViewApi = FTagViewApi(this@TestView)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    init {
        mTagViewApi.prepare { tagViewApi ->
            Log.i(TestView::class.java.simpleName, "FTagViewApi getViewTag:${tagViewApi.viewTag} getItem:${tagViewApi.getItem(TestItem::class.java)}")
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }
}