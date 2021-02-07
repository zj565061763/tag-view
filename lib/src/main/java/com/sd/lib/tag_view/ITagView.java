package com.sd.lib.tag_view;

import java.util.UUID;

public interface ITagView
{
    /** 空的View标识 */
    String EMPTY_VIEW_TAG = UUID.randomUUID().toString();

    /**
     * 返回View标识
     *
     * @return
     */
    String getViewTag();

    /**
     * Item管理对象
     *
     * @return
     */
    ItemManager getItemManager();

    interface ItemManager
    {
        /**
         * 查找指定类型的Item，如果不存在则返回null
         *
         * @param clazz
         * @param <T>
         * @return
         */
        <T extends Item> T findItem(Class<T> clazz);

        /**
         * 返回指定类型的Item，如果不存在则创建对象返回
         *
         * @param clazz
         * @param <T>
         * @return
         */
        <T extends Item> T getItem(Class<T> clazz);

        /**
         * 销毁Item
         */
        void destroyItem();
    }

    interface Item
    {
        /**
         * 初始化
         *
         * @param tagView
         */
        void init(ITagView tagView);

        /**
         * 销毁
         */
        void destroy();
    }
}