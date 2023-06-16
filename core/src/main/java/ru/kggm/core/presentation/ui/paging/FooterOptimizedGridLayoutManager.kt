package ru.kggm.core.presentation.ui.paging

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FooterOptimizedGridLayoutManager(
    context: Context,
    spanCount: Int,
    private val adapter: RecyclerView.Adapter<*>
) : GridLayoutManager(context, spanCount) {

    init {
        spanSizeLookup = spanSizeLookup
    }

    override fun getSpanSizeLookup(): SpanSizeLookup {
        return object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    position == adapter.itemCount && adapter.itemCount > 0 -> spanCount
                    else -> 1
                }
            }
        }
    }
}