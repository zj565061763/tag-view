package com.sd.demo.tag_view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.lib.tag_view.FTagViewManager;

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
        final FTagViewManager.ITagView tagView = FTagViewManager.getDefault().findTagView(this);
        Log.i(TestView.class.getSimpleName(), "findTagView:" + tagView);
    }
}
