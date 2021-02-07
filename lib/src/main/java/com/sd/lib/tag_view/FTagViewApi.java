package com.sd.lib.tag_view;

import android.view.View;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FTagViewApi implements ITagView.ItemManager
{
    private final View mView;
    private final Map<Runnable, String> mMapRunnable = new ConcurrentHashMap<>();

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
    public void prepare(final Runnable callback)
    {
        if (callback == null)
            return;

        if (isPrepared())
        {
            callback.run();
        } else
        {
            final Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    mMapRunnable.remove(this);
                    callback.run();
                }
            };

            mMapRunnable.put(runnable, "");
            mView.post(runnable);
        }
    }

    /**
     * 取消准备
     */
    public void cancelPrepare()
    {
        final Iterator<Runnable> it = mMapRunnable.keySet().iterator();
        while (it.hasNext())
        {
            final Runnable item = it.next();
            mView.removeCallbacks(item);
            it.remove();
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
    public void clearItem()
    {
        final ITagView tagView = findTagView();
        if (tagView == null)
            return;

        tagView.getItemManager().clearItem();
    }
}