package com.sd.demo.tag_view.item

import android.util.Log
import com.sd.lib.tag_view.ITagView

class TestItem : ITagView.Item {

    override fun initItem(tagView: ITagView) {
        Log.i(TestItem::class.java.simpleName, "initItem:$tagView $this")
    }

    override fun destroyItem() {
        Log.i(TestItem::class.java.simpleName, "destroyItem $this")
    }
}