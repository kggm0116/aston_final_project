package ru.kggm.core.presentation.utility

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

fun Context.getColorAttr(@AttrRes attrId: Int, @ColorInt default: Int = 0): Int {
    val attrs = obtainStyledAttributes(intArrayOf(attrId))
    val result = attrs.getColor(0, default)
    attrs.recycle()
    return result
}