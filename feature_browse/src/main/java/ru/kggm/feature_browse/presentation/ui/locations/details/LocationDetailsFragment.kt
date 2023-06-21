package ru.kggm.feature_browse.presentation.ui.locations.details

import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.feature_main.databinding.FragmentLocationDetailsBinding
import ru.kggm.feature_browse.di.LocationComponent
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import ru.kggm.feature_main.R

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
        binding.toolbar.apply {
            setNavigationIcon(ru.kggm.presentation.R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { navigateBack() }
            menu.clear()
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.location.collect { location ->
                location?.let {
                    displayLocation(it)
                    val characterFragment = CharacterListFragment().apply {
                        arguments = bundleOf(
                            CharacterListFragment.ARG_CHARACTER_IDS to it.residentIds
                        )
                    }
                    childFragmentManager.commit {
                        replace(R.id.fragment_container_residents, characterFragment)
                        addToBackStack(null)
                    }
                }
            }
        }
    }

    private fun displayLocation(location: LocationPresentationEntity) {
        binding.toolbar.title = location.name

        binding.info.textViewType.text = location.type
        binding.info.textViewDimension.text = location.dimension
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }
}