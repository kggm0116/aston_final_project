package ru.kggm.feature_browse.presentation.ui.episodes.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity

object EpisodeDiffUtil : DiffUtil.ItemCallback<EpisodePresentationEntity>() {
    override fun areItemsTheSame(
        oldItem: EpisodePresentationEntity,
        newItem: EpisodePresentationEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EpisodePresentationEntity,
        newItem: EpisodePresentationEntity
    ): Boolean {
        return oldItem == newItem
    }
}