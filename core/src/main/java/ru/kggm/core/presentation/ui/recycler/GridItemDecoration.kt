package ru.kggm.core.presentation.ui.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.toRectF
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.core.presentation.utility.dp2px
import kotlin.math.roundToInt

class GridItemDecoration(
    context: Context,
    @ColorInt backgrounColor: Int,
    marginDp: Float,
    cornerRadiusDp: Float
) : RecyclerView.ItemDecoration() {

    private val rect = Rect()
    private val paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = backgrounColor
        }
    }
    private val margin by lazy {
        context.dp2px(marginDp).roundToInt()
    }
    private val cornerRadius by lazy {
        context.dp2px(cornerRadiusDp)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(margin, margin, margin, margin)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position == RecyclerView.NO_POSITION) {
                continue
            }

            rect.set(view.left, view.top, view.right, view.bottom)
            val path = Path().apply {
                addRoundRect(
                    rect.toRectF(),
                    cornerRadius,
                    cornerRadius,
                    Path.Direction.CW
                )
            }

            canvas.drawPath(path, paint)
        }
    }


}