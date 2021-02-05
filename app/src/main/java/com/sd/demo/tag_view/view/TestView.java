package com.sd.demo.tag_view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.demo.tag_view.item.TestItem;
import com.sd.lib.tag_view.ext.FTagViewApi;

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

        final FTagViewApi tagViewApi = new FTagViewApi(this);
        Log.i(TestView.class.getSimpleName(), "onAttachedToWindow"
                + " getViewTag:" + tagViewApi.getViewTag()
                + " getItem:" + tagViewApi.getItem(TestItem.class));
    }
}
