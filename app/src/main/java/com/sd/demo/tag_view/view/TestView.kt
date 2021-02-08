package com.sd.demo.tag_view.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.sd.demo.tag_view.item.TestItem
import com.sd.demo.tag_view.view.TestView
import com.sd.lib.tag_view.FTagViewApi

class TestView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs)
{
    private val mTagViewApi = FTagViewApi(this@TestView)

    init
    {
        mTagViewApi.prepare { tagViewApi ->
            Log.i(TestView::class.simpleName, "FTagViewApi getViewTag:${tagViewApi.viewTag} getItem:${tagViewApi.getItem(TestItem::class.java)}")
        }
    }

    override fun onAttachedToWindow()
    {
        super.onAttachedToWindow()
    }
}