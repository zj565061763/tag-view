package com.sd.lib.tag_view;

import android.view.View;

import com.sd.lib.tag_view.FTagViewManager;
import com.sd.lib.tag_view.ITagView;

public class FTagViewApi implements ITagView.ItemManager
{
    private final View mView;

    public FTagViewApi(View view)
    {
        if (view == null)
            throw new IllegalArgumentException("view is null");
        mView = view;
    }

    private ITagView findTagView()
    {
        return FTagViewManager.getDefault().findTagView(mView);
    }

    public void prepare(Runnable runnable)
    {

    }

    /**
     * {@link ITagView#getViewTag()}
     *
     * @return
     */
    public String getViewTag()
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return ITagView.EMPTY_VIEW_TAG;

        return tagView.getViewTag();
    }

    @Override
    public <T extends ITagView.Item> T findItem(Class<T> clazz)
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return null;

        return tagView.getItemManager().findItem(clazz);
    }

    @Override
    public <T extends ITagView.Item> T getItem(Class<T> clazz)
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return null;

        return tagView.getItemManager().getItem(clazz);
    }

    @Override
    public void clearItem()
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return;

        tagView.getItemManager().clearItem();
    }
}