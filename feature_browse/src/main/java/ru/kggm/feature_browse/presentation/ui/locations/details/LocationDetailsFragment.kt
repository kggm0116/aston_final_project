package ru.kggm.feature_browse.presentation.ui.locations.details

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.feature_main.databinding.FragmentLocationDetailsBinding
import ru.kggm.feature_browse.di.LocationComponent
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
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
            launch {
                viewModel.location.collect { onLocationChanged(it) }
            }
        }
    }

    private fun onLocationChanged(result: LoadResult<LocationPresentationEntity?>) {
        binding.content.root.isVisible = result.state == LoadingState.Loaded
        binding.layoutLoading.root.isVisible = result.state == LoadingState.Loading
        binding.layoutError.root.isVisible = result.state == LoadingState.Error
        result.item?.let {
            displayLocation(it)
            initializeResidentsList(it) // What happens on config change?
        }
    }

    private fun displayLocation(location: LocationPresentationEntity) {
        binding.toolbar.title = location.name

        binding.content.info.textViewType.text = requireContext().getString(
            R.string.composite_text_location_type,
            location.type
        )
        binding.content.info.textViewDimension.text = requireContext().getString(
            R.string.composite_text_location_dimension,
            location.dimension
        )
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    private fun initializeResidentsList(location: LocationPresentationEntity) {
        val fragment = CharacterListFragment().apply {
            arguments = bundleOf(
                CharacterListFragment.ARG_CHARACTER_IDS to location.residentIds
            )
        }
        childFragmentManager.commit {
            replace(R.id.fragment_container_residents, fragment)
            addToBackStack(null)
        }
    }
}