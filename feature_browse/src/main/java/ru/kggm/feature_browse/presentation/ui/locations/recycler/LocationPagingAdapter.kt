package ru.kggm.feature_browse.presentation.ui.locations.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.shared.ItemClickable
import ru.kggm.feature_browse.databinding.LayoutLocationItemBinding

class LocationPagingAdapter : PagingDataAdapter<LocationPresentationEntity, LocationViewHolder>(
    LocationDiffUtil
), ItemClickable<LocationPresentationEntity> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutLocationItemBinding.inflate(inflater, parent, false)
        return LocationViewHolder(
            binding = binding,
            onLocationClicked = onItemClicked,
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)!!
        holder.bind(location)
    }

    override var onItemClicked: (LocationPresentationEntity) -> Unit = { }
}