package ru.kggm.feature_browse.presentation.ui.locations.recycler

import androidx.core.view.isVisible
import coil.load
import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_main.databinding.PagerItemLocationBinding

class LocationViewHolder(
    private val binding: PagerItemLocationBinding,
    private val onLocationClicked: (LocationPresentationEntity) -> Unit = { }
) : BaseViewHolder<PagerItemLocationBinding>(binding) {

    lateinit var location: LocationPresentationEntity

    fun bind(location: LocationPresentationEntity) {
        this.location = location
        binding.root.setDebouncedClickListener { onLocationClicked(location) }
        displayLocation()
    }
    
    private fun displayLocation() {
        with (location) {
            binding.textViewLocationName.text = name
            binding.textViewLocationType.text = type
            binding.textViewLocationDimension.text = dimension
        }
    }
}



