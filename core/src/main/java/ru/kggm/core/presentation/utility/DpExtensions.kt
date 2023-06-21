package ru.kggm.core.presentation.utility

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

fun Context.dp2px(value: Float) = resources.displayMetrics.density * value

fun View.dp2px(value: Float) = resources.displayMetrics.density * value

fun Fragment.dp2px(value: Float) = resources.displayMetrics.density * value