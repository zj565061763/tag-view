package com.sd.lib.tag_view.ext;

import android.view.View;

import com.sd.lib.tag_view.FTagViewManager;
import com.sd.lib.tag_view.ITagView;
import com.sd.lib.tag_view.ITagView.ItemManager;

public class FTagViewApi
{
    private final View mView;

    public FTagViewApi(View view)
    {
        if (view == null)
            throw new IllegalArgumentException("view is null");
        mView = view;
    }

    /**
     * {@link FTagViewManager#findTagView(View)}
     *
     * @return
     */
    private ITagView findTagView()
    {
        return FTagViewManager.getDefault().findTagView(mView);
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
            return null;

        return tagView.getViewTag();
    }

    /**
     * {@link ItemManager#findItem(Class)}
     *
     * @return
     */
    public <T extends ITagView.Item> T findItem(Class<T> clazz)
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return null;

        final ITagView.ItemManager itemManager = tagView.getItemManager();
        return itemManager.findItem(clazz);
    }

    /**
     * {@link ItemManager#getItem(Class)}
     *
     * @return
     */
    public <T extends ITagView.Item> T getItem(Class<T> clazz)
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return null;

        final ITagView.ItemManager itemManager = tagView.getItemManager();
        return itemManager.getItem(clazz);
    }

    /**
     * {@link ItemManager#clearItem()}
     */
    public void clearItem()
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return;

        final ITagView.ItemManager itemManager = tagView.getItemManager();
        itemManager.clearItem();
    }
}