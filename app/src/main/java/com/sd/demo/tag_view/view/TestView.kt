package com.sd.demo.tag_view.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.sd.demo.tag_view.item.TestItem
import com.sd.lib.tag_view.FTagViewApi
import com.sd.lib.tag_view.ITagView

class TestView : FrameLayout {
    private val _tagViewApi = FTagViewApi(this@TestView)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        _tagViewApi.prepare {
            Log.i(
                TestView::class.java.simpleName,
                "FTagViewApi getViewTag:${it.viewTag} getItem:${it.getItem(TestItem::class.java)}"
            )
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val customTagView = ITagView.find<CustomTagView>(this)!!
        val customViewTag = ITagView.findTag<CustomTagView>(this)
        Log.i(
            TestView::class.java.simpleName,
            "CustomTagView getViewTag:${customTagView.viewTag} getViewTag:${customViewTag}"
        )
    }
}