package ru.kggm.feature_browse.presentation.ui.episodes.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_main.databinding.PagerItemEpisodeBinding

class EpisodePagingAdapter : PagingDataAdapter<EpisodePresentationEntity, EpisodeViewHolder>(
    EpisodeDiffUtil
) {

    var onEpisodeClicked: (EpisodePresentationEntity) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PagerItemEpisodeBinding.inflate(inflater, parent, false)
        return EpisodeViewHolder(
            binding = binding,
            onEpisodeClicked = onEpisodeClicked,
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val character = getItem(position)!!
        holder.bind(character)
    }
}