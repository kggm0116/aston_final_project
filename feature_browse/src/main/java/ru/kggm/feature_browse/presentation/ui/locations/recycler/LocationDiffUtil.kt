package ru.kggm.feature_browse.presentation.ui.locations.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity

object LocationDiffUtil : DiffUtil.ItemCallback<LocationPresentationEntity>() {
    override fun areItemsTheSame(
        oldItem: LocationPresentationEntity,
        newItem: LocationPresentationEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: LocationPresentationEntity,
        newItem: LocationPresentationEntity
    ): Boolean {
        return oldItem == newItem
    }
}