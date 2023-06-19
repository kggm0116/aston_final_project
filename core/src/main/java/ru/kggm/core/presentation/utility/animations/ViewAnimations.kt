package ru.kggm.core.presentation.utility.animations

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.annotation.AnimRes

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
    val targetVisibility = visibility.toViewVisibility()
    if (this.visibility == targetVisibility)
        return

    val currentlyVisible = this.visibility == View.VISIBLE
    val alpha = if (currentlyVisible) 0f else 1f
    this.animate()
        .alpha(alpha)
        .setDuration(duration)
        .withEndAction {
            this.visibility = targetVisibility
            endAction()
        }
        .start()
}

fun View.animateVisibility(
    duration: Long = DURATION_VIEW_ANIMATION_DEFAULT_MS,
    endAction: () -> Unit = { },
    visibleOrGone: () -> Boolean
) {
    val visibility = if (visibleOrGone()) Visibility.Visible else Visibility.Gone
    val targetVisibility = visibility.toViewVisibility()
    if (this.visibility == targetVisibility)
        return

    val currentlyVisible = this.visibility == View.VISIBLE
    val alpha = if (currentlyVisible) 0f else 1f
    this.animate()
        .alpha(alpha)
        .setDuration(duration)
        .withEndAction {
            this.visibility = targetVisibility
            endAction()
        }
        .start()
}

fun View.animateVisibility(
    visibility: Visibility,
    @AnimRes animation: Int,
    endAction: () -> Unit = { },
) {
    val targetVisibility = visibility.toViewVisibility()
    if (this.visibility == targetVisibility)
        return

    val anim = AnimationUtils.loadAnimation(this.context, animation).apply {
        setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                this@animateVisibility.visibility = targetVisibility
                endAction()
            }
        })
    }

    startAnimation(anim)
    this.visibility = targetVisibility
}