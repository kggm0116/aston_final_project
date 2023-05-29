package ru.kggm.core.presentation.utility

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment

val View.inLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
val View.inPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

val Fragment.inLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
val Fragment.inPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

fun runOnUiThread(block: () -> Unit) {
    Handler(Looper.getMainLooper()).post(block)
}