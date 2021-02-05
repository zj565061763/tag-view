package com.sd.lib.tag_view;

public interface ITagView
{
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
         * 清空并销毁Item
         */
        void clearItem();
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