package com.sd.lib.tag_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FTagView extends FrameLayout implements FTagViewManager.ITagView
{
    private final String mViewTag;

    public FTagView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mViewTag = createTag(this);
    }

    @Override
    public String getViewTag()
    {
        return mViewTag;
    }

    /**
     * 创建tag
     *
     * @param object
     * @return
     */
    public static String createTag(Object object)
    {
        final String className = object.getClass().getName();
        final String hashCode = Integer.toHexString(System.identityHashCode(object));
        return className + "@" + hashCode;
    }
}
