package com.sd.lib.tag_view;

import android.view.View;

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

    /**
     * 是否已经准备好
     *
     * @return
     */
    public boolean isPrepared()
    {
        return Utils.isAttached(mView);
    }

    /**
     * 准备调用当前对象的api
     *
     * @param callback
     */
    public void prepare(final PrepareCallback callback)
    {
        if (callback == null)
            return;

        if (isPrepared())
        {
            callback.onPrepared(FTagViewApi.this);
        } else
        {
            mView.post(new Runnable()
            {
                @Override
                public void run()
                {
                    prepare(callback);
                }
            });
        }
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
    public void destroyItem()
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return;

        tagView.getItemManager().destroyItem();
    }

    public interface PrepareCallback
    {
        void onPrepared(FTagViewApi tagViewApi);
    }
}