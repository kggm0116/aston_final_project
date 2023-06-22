package ru.kggm.core.presentation.utility

import android.os.SystemClock
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

private const val DEBOUNCE_DELAY_MS = 500L

fun clickableSpan(
    styling: TextPaint.() -> Unit = { },
    clickDelay: Long = DEBOUNCE_DELAY_MS,
    onClick: () -> Unit
): ClickableSpan = object : ClickableSpan() {

    private var lastClickTime = 0L
    override fun onClick(widget: View) {
        val currentTime = SystemClock.uptimeMillis()
        if (lastClickTime == 0L || currentTime - lastClickTime > clickDelay) {
            lastClickTime = currentTime
            onClick()
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        styling(ds)
    }
}