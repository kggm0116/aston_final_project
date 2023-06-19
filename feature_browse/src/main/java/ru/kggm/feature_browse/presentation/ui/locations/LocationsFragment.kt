package ru.kggm.feature_browse.presentation.ui.locations

import androidx.fragment.app.commit
import ru.kggm.core.presentation.ui.fragments.fragment.ViewBindingFragment
import ru.kggm.feature_browse.presentation.ui.locations.list.LocationListFragment
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentLocationsBinding

class LocationsFragment : ViewBindingFragment<FragmentLocationsBinding>() {
    override fun createBinding() = FragmentLocationsBinding.inflate(layoutInflater)

    override fun onInitialize() {

    }
}