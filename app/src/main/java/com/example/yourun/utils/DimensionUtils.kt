package com.example.yourun.utils

import android.content.Context

object DimensionUtils {
    /**
     * Convert DP to PX
     * @param context Context to access resources
     * @param dp DP value to convert
     * @return PX value as Int
     */
    fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
