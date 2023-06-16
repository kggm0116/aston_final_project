package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.feature_main.R

class CharacterLayoutManager(
    context: Context,
    spanCount: Int,
    private val adapter: RecyclerView.Adapter<*>
) : GridLayoutManager(context, spanCount) {

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