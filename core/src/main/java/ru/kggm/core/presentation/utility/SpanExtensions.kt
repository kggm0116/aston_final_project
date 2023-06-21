package ru.kggm.core.presentation.utility

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

fun String.toClickableSpan(
    styling: TextPaint.() -> Unit = { },
    onClick: () -> Unit
): ClickableSpan = object : ClickableSpan() {
    override fun onClick(widget: View) {
        onClick()
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        styling(ds)
    }
}