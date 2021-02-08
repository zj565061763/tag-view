package com.sd.lib.tag_view

import android.os.Build
import android.view.View

internal class Utils {
    companion object {
        @JvmStatic
        fun isAttached(view: View?): Boolean {
            if (view == null) return false
            return if (Build.VERSION.SDK_INT >= 19) view.isAttachedToWindow else view.windowToken != null
        }

        @JvmStatic
        fun getObjectId(obj: Any): String {
            val className = obj.javaClass.name
            val identityHashCode = System.identityHashCode(obj)
            return className + "@" + Integer.toHexString(identityHashCode)
        }
    }
}