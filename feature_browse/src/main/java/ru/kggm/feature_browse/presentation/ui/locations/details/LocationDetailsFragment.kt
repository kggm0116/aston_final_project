package ru.kggm.feature_browse.presentation.ui.locations.details

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.fragment.ViewModelFragment
import ru.kggm.feature_main.databinding.FragmentLocationDetailsBinding
import ru.kggm.feature_browse.di.LocationComponent
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity

class LocationDetailsFragment :
    ViewModelFragment<FragmentLocationDetailsBinding, LocationDetailsViewModel>(
        LocationDetailsViewModel::class.java,
    ) {

    companion object {
        const val ARG_LOCATION_ID = "ARG_LOCATION_ID"
    }

    override fun createBinding() = FragmentLocationDetailsBinding.inflate(layoutInflater)
    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        LocationComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun viewModelOwner() = this

    private val locationId by lazy {
        requireNotNull(arguments?.getLong(ARG_LOCATION_ID)) { "Could not retrieve location id" }
    }

    override fun onInitialize() {
        viewModel.loadLocation(locationId)
        subscribeToViewModel()
        initializeToolbar()
    }

    private fun initializeToolbar() {
        binding.toolbarLocationDetails.apply {
            setNavigationIcon(ru.kggm.presentation.R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { navigateBack() }
            menu.clear()
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.location.collect { location ->
                location?.let { displayLocation(it) }
            }
        }
    }

    private fun displayLocation(location: LocationPresentationEntity) {
        with(binding.layoutLocationDetails) {
            binding.toolbarLocationDetails.title = location.name

            textViewLocationType.text = location.type
            textViewLocationDimension.text = location.dimension
        }
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }
}