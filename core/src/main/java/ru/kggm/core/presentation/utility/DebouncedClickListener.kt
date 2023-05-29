package ru.kggm.core.presentation.utility

import android.os.SystemClock
import android.view.View

private const val DEBOUNCE_DELAY_MS = 300L

fun View.setDebouncedClickListener(
    delay: Long = DEBOUNCE_DELAY_MS,
    onClickAction: () -> Unit
) {
    isClickable = true
    setOnClickListener(getDebouncedClickListener(delay, onClickAction))
}

fun getDebouncedClickListener(
    delay: Long,
    onClickAction: () -> Unit
): View.OnClickListener {
    return object : View.OnClickListener {

        private var lastClickTime = 0L

        override fun onClick(v: View) {
            val currentTime = SystemClock.uptimeMillis()
            if (lastClickTime == 0L || currentTime - lastClickTime > delay) {
                lastClickTime = currentTime
                onClickAction()
            }
        }
    }
}