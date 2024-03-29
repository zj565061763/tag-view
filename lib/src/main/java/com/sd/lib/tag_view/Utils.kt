package com.sd.lib.tag_view

import android.os.Build
import android.view.View

internal object Utils {
    @JvmStatic
    fun isAttached(view: View?): Boolean {
        if (view == null) return false

        return if (Build.VERSION.SDK_INT >= 19)
            view.isAttachedToWindow
        else
            view.windowToken != null
    }

    @JvmStatic
    fun getObjectId(obj: Any): String {
        return "${obj.javaClass.name}@${getHashString(obj)}"
    }

    @JvmStatic
    fun getHashString(obj: Any): String {
        val hashCode = System.identityHashCode(obj)
        return Integer.toHexString(hashCode)
    }
}