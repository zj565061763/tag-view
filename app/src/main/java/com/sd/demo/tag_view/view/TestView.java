package com.sd.demo.tag_view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.demo.tag_view.item.TestItem;
import com.sd.lib.tag_view.FTagViewManager;
import com.sd.lib.tag_view.ITagView;

public class TestView extends FrameLayout
{
    public TestView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        final ITagView tagView = FTagViewManager.getDefault().findTagView(this);
        final TestItem testItem = tagView.getItemManager().getItem(TestItem.class);

        Log.i(TestView.class.getSimpleName(), "onAttachedToWindow"
                + " tagView:" + tagView
                + " testItem:" + testItem);
    }
}
