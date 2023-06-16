package ru.kggm.core.presentation.utility.animations

import android.view.View

private const val DURATION_VIEW_ANIMATION_DEFAULT_MS = 200L

enum class Visibility {
    Visible, Invisible, Gone;
    fun toViewVisibility() = when (this) {
        Visible -> View.VISIBLE
        Invisible -> View.INVISIBLE
        Gone -> View.GONE
    }
}

fun View.animateVisibility(
    visibility: Visibility,
    duration: Long = DURATION_VIEW_ANIMATION_DEFAULT_MS
) {
    val viewVisibility = visibility.toViewVisibility()

    val isVisible = this.visibility == View.VISIBLE
    if (isVisible && viewVisibility == View.GONE || !isVisible && viewVisibility == View.VISIBLE) {
        val alpha = if (isVisible) 1f else 0f
        this.animate()
            .alpha(alpha)
            .setDuration(duration)
            .withEndAction { this.visibility = viewVisibility }
            .start()
    }
}