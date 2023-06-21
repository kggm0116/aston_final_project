package ru.kggm.feature_browse.presentation.ui.locations.recycler

import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_main.databinding.LayoutLocationItemBinding

class LocationViewHolder(
    private val binding: LayoutLocationItemBinding,
    private val onLocationClicked: (LocationPresentationEntity) -> Unit = { }
) : BaseViewHolder<LayoutLocationItemBinding>(binding) {

    lateinit var location: LocationPresentationEntity

    fun bind(location: LocationPresentationEntity) {
        this.location = location
        binding.root.setDebouncedClickListener { onLocationClicked(location) }
        displayLocation()
    }
    
    private fun displayLocation() {
        with (location) {
            binding.info.textViewLocationName.text = name
            binding.info.textViewType.text = type
            binding.info.textViewDimension.text = dimension
        }
    }
}



