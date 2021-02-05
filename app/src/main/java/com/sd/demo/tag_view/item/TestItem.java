package com.sd.demo.tag_view.item;

import android.util.Log;

import com.sd.lib.tag_view.ITagView;

public class TestItem implements ITagView.Item
{
    private static final String TAG = TestItem.class.getSimpleName();

    @Override
    public void init(ITagView tagView)
    {
        Log.i(TAG, "init:" + tagView + " " + this);
    }

    @Override
    public void destroy()
    {
        Log.i(TAG, "destroy " + " " + this);
    }
}
