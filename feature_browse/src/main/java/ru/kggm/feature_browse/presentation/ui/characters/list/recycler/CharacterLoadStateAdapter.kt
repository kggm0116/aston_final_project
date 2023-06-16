package ru.kggm.feature_browse.presentation.ui.characters.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import ru.kggm.feature_main.databinding.PagerFooterCharacterLoadStateBinding

class CharacterLoadStateAdapter(
    private val retry: () -> Unit = { }
) : LoadStateAdapter<CharacterLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CharacterLoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PagerFooterCharacterLoadStateBinding.inflate(
            inflater, parent, false
        )
        return CharacterLoadStateViewHolder(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: CharacterLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
