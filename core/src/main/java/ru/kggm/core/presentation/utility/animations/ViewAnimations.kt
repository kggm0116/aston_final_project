package ru.kggm.core.presentation.utility.animations

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

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
    endAction: () -> Unit = { },
    duration: Long = DURATION_VIEW_ANIMATION_DEFAULT_MS
) {
    val viewVisibility = visibility.toViewVisibility()
    if (this.visibility == viewVisibility)
        return

    val isVisible = this.visibility == View.VISIBLE
    if (isVisible && viewVisibility == View.GONE || !isVisible && viewVisibility == View.VISIBLE) {
        val alpha = if (isVisible) 1f else 0f
        this.animate()
            .alpha(alpha)
            .setDuration(duration)
            .withEndAction {
                this.visibility = viewVisibility
                endAction()
            }
            .start()
    }
}

fun View.animateVisibility(
    duration: Long = DURATION_VIEW_ANIMATION_DEFAULT_MS,
    endAction: () -> Unit = { },
    visibleOrGone: () -> Boolean
) {
    val visibility = if (visibleOrGone()) Visibility.Visible else Visibility.Gone
    val viewVisibility = visibility.toViewVisibility()
    if (this.visibility == viewVisibility)
        return

    val isVisible = this.visibility == View.VISIBLE
    if (isVisible && viewVisibility == View.GONE || !isVisible && viewVisibility == View.VISIBLE) {
        val alpha = if (isVisible) 1f else 0f
        this.animate()
            .alpha(alpha)
            .setDuration(duration)
            .withEndAction {
                this.visibility = viewVisibility
                endAction()
            }
            .start()
    }
}

fun View.animateVisibility(
    visibility: Visibility,
    @AnimRes animation: Int,
    endAction: () -> Unit = { },
) {
    val viewVisibility = visibility.toViewVisibility()
    if (this.visibility == viewVisibility)
        return

    val anim = AnimationUtils.loadAnimation(this.context, animation).apply {
        setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                this@animateVisibility.visibility = viewVisibility
                endAction()
            }
        })
    }

    startAnimation(anim)
    this.visibility = viewVisibility
}