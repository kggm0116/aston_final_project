package ru.kggm.feature_browse.presentation.ui.locations.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_main.databinding.PagerItemLocationBinding

class LocationPagingAdapter : PagingDataAdapter<LocationPresentationEntity, LocationViewHolder>(
    LocationDiffUtil
) {

    var onLocationClicked: (LocationPresentationEntity) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PagerItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(
            binding = binding,
            onLocationClicked = onLocationClicked,
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)!!
        holder.bind(location)
    }
}