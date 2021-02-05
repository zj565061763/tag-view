package com.sd.lib.tag_view;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FTagViewManager
{
    private static FTagViewManager sDefault;

    public static FTagViewManager getDefault()
    {
        if (sDefault != null)
            return sDefault;

        synchronized (FTagViewManager.class)
        {
            if (sDefault == null)
                sDefault = new FTagViewManager();
            return sDefault;
        }
    }

    private final Map<View, ITagView> mMapViewCache = new ConcurrentHashMap<>();

    private boolean mIsDebug;

    public FTagViewManager()
    {
        // 构造方法保持public，可以不使用默认对象
    }

    /**
     * 设置是否调试模式
     *
     * @param debug
     */
    public void setDebug(boolean debug)
    {
        mIsDebug = debug;
    }

    /**
     * 查找{@link ITagView}
     *
     * @param view
     * @return
     */
    public ITagView findTagView(View view)
    {
        if (!Utils.isAttached(view))
            return null;

        if (mIsDebug)
        {
            Log.i(FTagViewManager.class.getSimpleName(), "findTagView"
                    + " view:" + Utils.getObjectId(view)
                    + " ---------->");
        }

        ITagView tagView = checkTagView(view);
        if (tagView != null)
        {
            if (mIsDebug)
            {
                Log.i(FTagViewManager.class.getSimpleName(), "findTagView"
                        + " view:" + Utils.getObjectId(view)
                        + " tagView:" + Utils.getObjectId(tagView));
            }
            return tagView;
        }

        final List<View> listChild = new LinkedList<>();
        listChild.add(view);

        ViewParent viewParent = view.getParent();
        while (viewParent instanceof View)
        {
            final View parent = (View) viewParent;
            if (!Utils.isAttached(parent))
                return null;

            tagView = checkTagView(parent);
            if (tagView != null)
            {
                cacheView(tagView, listChild);
                if (mIsDebug)
                {
                    Log.i(FTagViewManager.class.getSimpleName(), "findTagView"
                            + " view:" + Utils.getObjectId(view)
                            + " tagView:" + Utils.getObjectId(tagView)
                            + " level:" + listChild.size()
                            + " cacheSize:" + mMapViewCache.size()
                    );
                }
                return tagView;
            } else
            {
                listChild.add(parent);
                viewParent = parent.getParent();
            }
        }

        throw new RuntimeException(ITagView.class.getSimpleName() + " was not found int view tree " + view);
    }

    private ITagView checkTagView(View view)
    {
        if (view instanceof ITagView)
        {
            final ITagView tagView = (ITagView) view;
            return tagView;
        } else
        {
            return mMapViewCache.get(view);
        }
    }

    /**
     * 缓存View
     *
     * @param tagView
     * @param list
     */
    private void cacheView(ITagView tagView, List<View> list)
    {
        synchronized (FTagViewManager.this)
        {
            for (View view : list)
            {
                if (!Utils.isAttached(view))
                    continue;

                final ITagView put = mMapViewCache.put(view, tagView);
                if (put == null)
                    view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }
        }
    }

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            v.removeOnAttachStateChangeListener(this);
            synchronized (FTagViewManager.this)
            {
                mMapViewCache.remove(v);
            }
        }
    };
}