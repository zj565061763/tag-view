package com.sd.demo.tag_view.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.sd.lib.tag_view.ITagView

class CustomTagView : FrameLayout, ITagView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override val viewTag: String?
        get() {
            val hashCode = System.identityHashCode(this)
            val hashCodeString = Integer.toHexString(hashCode)
            return "${javaClass.name}@${hashCodeString}"
        }
}