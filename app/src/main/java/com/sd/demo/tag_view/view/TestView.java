package com.sd.demo.tag_view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.demo.tag_view.item.TestItem;
import com.sd.lib.tag_view.FTagViewApi;

public class TestView extends FrameLayout
{
    private final FTagViewApi mTagViewApi = new FTagViewApi(TestView.this);

    public TestView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mTagViewApi.prepare(new Runnable()
        {
            @Override
            public void run()
            {
                Log.i(TestView.class.getSimpleName(), "FTagViewApi"
                        + " getViewTag:" + mTagViewApi.getViewTag()
                        + " getItem:" + mTagViewApi.getItem(TestItem.class));
            }
        });
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }
}
