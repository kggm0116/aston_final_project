package ru.kggm.feature_browse.presentation.ui.episodes.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.ItemClickable
import ru.kggm.feature_main.databinding.LayoutEpisodeItemBinding

class EpisodePagingAdapter : PagingDataAdapter<EpisodePresentationEntity, EpisodeViewHolder>(
    EpisodeDiffUtil
), ItemClickable<EpisodePresentationEntity> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutEpisodeItemBinding.inflate(inflater, parent, false)
        return EpisodeViewHolder(
            binding = binding,
            onEpisodeClicked = onItemClicked,
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val character = getItem(position)!!
        holder.bind(character)
    }

    override var onItemClicked: (EpisodePresentationEntity) -> Unit = { }
}