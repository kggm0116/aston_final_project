package ru.kggm.core.presentation.ui.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import ru.kggm.core.databinding.PagerFooterLoadStateBinding

class CommonLoadStateAdapter(
    private val onRetry: () -> Unit
) : LoadStateAdapter<CommonLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): CommonLoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PagerFooterLoadStateBinding.inflate(
            inflater, parent, false
        )
        return CommonLoadStateViewHolder(binding = binding, onRetry = onRetry)
    }

    override fun onBindViewHolder(holder: CommonLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
