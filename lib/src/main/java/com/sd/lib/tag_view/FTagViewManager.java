package com.sd.lib.tag_view;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    /** 空的Tag */
    public static final String TAG_EMPTY = UUID.randomUUID().toString();

    private final Map<ITagView, ViewTree> mMapTagViewTree = new ConcurrentHashMap<>();
    private final Map<View, ViewTree> mMapViewTreeCache = new ConcurrentHashMap<>();

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
     * 查找View对应的tag
     *
     * @param view
     * @return
     */
    public synchronized String findViewTag(View view)
    {
        if (!isAttached(view))
            return TAG_EMPTY;

        if (mIsDebug)
        {
            Log.i(FTagViewManager.class.getSimpleName(), "findViewTag"
                    + " view:" + getObjectId(view)
                    + " ---------->");
        }

        final ViewTree tree = findViewTree(view);
        if (tree != null)
        {
            if (mIsDebug)
            {
                Log.i(FTagViewManager.class.getSimpleName(), "findViewTag"
                        + " view:" + getObjectId(view)
                        + " viewTree:" + tree);
            }

            return tree.getViewTag();
        }

        final List<View> listChild = new LinkedList<>();
        listChild.add(view);

        ViewParent viewParent = view.getParent();
        while (viewParent != null && viewParent instanceof View)
        {
            final View parent = (View) viewParent;
            if (!isAttached(parent))
                return TAG_EMPTY;

            final ViewTree viewTree = findViewTree(parent);
            if (viewTree != null)
            {
                viewTree.addViews(listChild);

                if (mIsDebug)
                {
                    Log.i(FTagViewManager.class.getSimpleName(), "findViewTag"
                            + " view:" + getObjectId(view)
                            + " viewTree:" + viewTree
                            + " level:" + listChild.size()
                            + " viewTreeSize:" + mMapTagViewTree.size()
                            + " cacheTreeSize:" + mMapViewTreeCache.size()
                    );
                }
                return viewTree.getViewTag();
            } else
            {
                listChild.add(parent);
                viewParent = parent.getParent();
            }
        }

        throw new RuntimeException(ITagView.class.getSimpleName() + " was not found int view tree " + view);
    }

    private ViewTree findViewTree(View view)
    {
        if (view instanceof ITagView)
        {
            final ITagView tagView = (ITagView) view;
            return getOrCreateViewTree(tagView);
        }

        return mMapViewTreeCache.get(view);
    }

    private synchronized ViewTree getOrCreateViewTree(ITagView tagView)
    {
        if (tagView == null)
            throw new IllegalArgumentException("tagView is null");

        ViewTree viewTree = mMapTagViewTree.get(tagView);
        if (viewTree == null)
        {
            viewTree = new ViewTree(tagView);
            mMapTagViewTree.put(tagView, viewTree);
            initTagView(tagView);

            if (mIsDebug)
            {
                Log.i(FTagViewManager.class.getSimpleName(), "create ViewTree"
                        + " tagView:" + getObjectId(tagView)
                        + " viewTree:" + viewTree
                        + " viewTreeSize:" + mMapTagViewTree.size()
                        + " cacheTreeSize:" + mMapViewTreeCache.size());
            }
        }
        return viewTree;
    }

    private synchronized void removeViewTree(ITagView tagView)
    {
        if (tagView == null)
            throw new IllegalArgumentException("tagView is null");

        final ViewTree viewTree = mMapTagViewTree.remove(tagView);

        if (mIsDebug)
        {
            Log.i(FTagViewManager.class.getSimpleName(), "remove ViewTree"
                    + " tagView:" + getObjectId(tagView)
                    + " viewTree:" + viewTree
                    + " viewTreeSize:" + mMapTagViewTree.size()
                    + " cacheTreeSize:" + mMapViewTreeCache.size());
        }
    }

    private void initTagView(final ITagView tagView)
    {
        final View view = (View) tagView;
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
        {
            @Override
            public void onViewAttachedToWindow(View v)
            {
            }

            @Override
            public void onViewDetachedFromWindow(View v)
            {
                v.removeOnAttachStateChangeListener(this);
                removeViewTree(tagView);
            }
        });
    }

    private final class ViewTree implements View.OnAttachStateChangeListener
    {
        private final ITagView nTagView;
        private final Map<View, String> nMapView = new ConcurrentHashMap<>();

        public ViewTree(ITagView tagView)
        {
            if (tagView == null)
                throw new IllegalArgumentException("tagView is null");

            this.nTagView = tagView;
        }

        public String getViewTag()
        {
            final View view = (View) nTagView;
            return isAttached(view) ? nTagView.getViewTag() : TAG_EMPTY;
        }

        public void addViews(List<View> views)
        {
            for (View view : views)
            {
                if (isAttached(view))
                {
                    final String put = nMapView.put(view, "");
                    if (put == null)
                    {
                        mMapViewTreeCache.put(view, this);
                        view.addOnAttachStateChangeListener(this);
                    }
                }
            }
        }

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
                nMapView.remove(v);
                mMapViewTreeCache.remove(v);
            }
        }
    }

    public interface ITagView
    {
        String getViewTag();
    }

    private static boolean isAttached(View view)
    {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }

    private static String getObjectId(Object object)
    {
        final String className = object.getClass().getName();
        final String hashCode = Integer.toHexString(System.identityHashCode(object));
        return className + "@" + hashCode;
    }
}