package com.sd.demo.tag_view.item

import android.util.Log
import com.sd.lib.tag_view.ITagView

class TestItem : ITagView.Item
{
    override fun init(tagView: ITagView)
    {
        Log.i(TestItem::class.simpleName, "init:$tagView $this")
    }

    override fun destroy()
    {
        Log.i(TestItem::class.simpleName, "destroy $this")
    }
}