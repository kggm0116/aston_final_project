package ru.kggm.core.presentation.ui.paging

import android.util.Log
import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.core.utility.classTag
import ru.kggm.core.databinding.PagerFooterLoadStateBinding

class CommonLoadStateViewHolder(
    private val binding: PagerFooterLoadStateBinding,
    private val onRetry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        Log.i(classTag(), "Load state: $loadState")
        when (loadState) {
            is LoadState.Loading -> {
                binding.layoutPagerFooterLoading.root.visibility = View.VISIBLE
                binding.layoutPagerFooterError.root.visibility = View.GONE
            }
            is LoadState.Error -> {
                binding.layoutPagerFooterLoading.root.visibility = View.GONE
                binding.layoutPagerFooterError.root.visibility = View.VISIBLE
            }
            is LoadState.NotLoading -> {
                binding.layoutPagerFooterLoading.root.visibility = View.GONE
                binding.layoutPagerFooterError.root.visibility = View.GONE
            }
        }
        binding.layoutPagerFooterError.buttonPagerFooterRetry.setDebouncedClickListener { onRetry() }
    }
}
