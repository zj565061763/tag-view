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
        mViewTag = createTag(this);
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
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        return null;
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
                    throw new NullPointerException("createItem return null");

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
            throw new NullPointerException("clazz is null");

        if (clazz.isInterface())
            throw new IllegalArgumentException("clazz is interface " + clazz);

        if (Modifier.isAbstract(clazz.getModifiers()))
            throw new IllegalArgumentException("clazz is abstract " + clazz);
    }

    /**
     * 创建tag
     *
     * @param object
     * @return
     */
    public static String createTag(Object object)
    {
        final String className = object.getClass().getName();
        final String hashCode = Integer.toHexString(System.identityHashCode(object));
        return className + "@" + hashCode;
    }
}
