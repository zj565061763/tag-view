package com.sd.lib.tag_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FTagView extends FrameLayout implements ITagView
{
    private final String mViewTag;
    private final ItemManager mItemManager;

    public FTagView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mViewTag = Utils.getObjectId(this);
        mItemManager = new InternalItemManager();
    }

    @Override
    public final String getViewTag()
    {
        return mViewTag;
    }

    @Override
    public final ItemManager getItemManager()
    {
        return mItemManager;
    }

    protected <T extends Item> T createItem(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        } catch (Exception e)
        {
            throw new RuntimeException("Create instance failed for class:" + clazz.getName(), e);
        }
    }

    private final class InternalItemManager implements ItemManager
    {
        private final Map<Class<? extends Item>, Item> nItemHolder = new ConcurrentHashMap<>();

        @Override
        public synchronized <T extends Item> T findItem(Class<T> clazz)
        {
            checkItemClass(clazz);

            final Item item = nItemHolder.get(clazz);
            if (item == null)
                return null;
            return (T) item;
        }

        @Override
        public synchronized <T extends Item> T getItem(Class<T> clazz)
        {
            checkItemClass(clazz);

            Item item = nItemHolder.get(clazz);
            if (item == null)
            {
                item = createItem(clazz);
                if (item == null)
                    throw new NullPointerException("createItem method return null");

                nItemHolder.put(clazz, item);
                item.init(FTagView.this);
            }
            return (T) item;
        }

        @Override
        public synchronized void clearItem()
        {
            for (Item item : nItemHolder.values())
            {
                item.destroy();
            }
            nItemHolder.clear();
        }
    }

    private static <T extends Item> void checkItemClass(Class<T> clazz)
    {
        if (clazz == null)
            throw new IllegalArgumentException("clazz is null");

        if (clazz.isInterface())
            throw new IllegalArgumentException("clazz is interface " + clazz);

        if (Modifier.isAbstract(clazz.getModifiers()))
            throw new IllegalArgumentException("clazz is abstract " + clazz);
    }
}
