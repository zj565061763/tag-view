package com.sd.lib.tag_view;

import android.os.Build;
import android.view.View;

class Utils
{
    private Utils()
    {
    }

    public static boolean isAttached(View view)
    {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }

    public static String getObjectId(Object obj)
    {
        final String className = obj.getClass().getName();
        final String hashCode = Integer.toHexString(System.identityHashCode(obj));
        return className + "@" + hashCode;
    }
}