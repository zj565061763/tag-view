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
        mTagViewApi.prepare(new FTagViewApi.PrepareCallback()
        {
            @Override
            public void onPrepared(FTagViewApi tagViewApi)
            {
                Log.i(TestView.class.getSimpleName(), "FTagViewApi"
                        + " getViewTag:" + tagViewApi.getViewTag()
                        + " getItem:" + tagViewApi.getItem(TestItem.class));
            }
        });
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }
}
