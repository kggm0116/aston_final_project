package ru.kggm.feature_browse.presentation.ui.locations.list

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.recycler.StyledItemDecoration
import ru.kggm.core.presentation.utility.getColorAttr
import ru.kggm.feature_browse.di.LocationComponent
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.locations.details.LocationDetailsFragment
import ru.kggm.feature_browse.presentation.ui.locations.recycler.LocationPagingAdapter
import ru.kggm.feature_browse.presentation.ui.shared.BaseListFragment
import ru.kggm.feature_browse.presentation.ui.shared.ListNetworkState
import ru.kggm.feature_browse.presentation.ui.shared.openDetailsFragment
import ru.kggm.feature_main.databinding.LayoutListBinding

class LocationListFragment :
    BaseListFragment<LocationListViewModel, LocationPagingAdapter, LocationPresentationEntity>(
        LocationListViewModel::class.java,
    ) {
    companion object {
        const val ARG_LOCATION_IDS = "ARG_LOCATION_IDS"
    }

    private val locationIds by lazy {
        arguments?.getIntegerArrayList(ARG_LOCATION_IDS)?.toList()
    }

    override val filterEnabled by lazy { locationIds != null }

    override fun createBinding() = LayoutListBinding.inflate(layoutInflater)
    override fun viewModelOwner(): ViewModelStoreOwner = if (locationIds != null) {
        requireParentFragment()
    } else {
        requireActivity()
    }

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        LocationComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        super.onInitialize()
        viewModel.setIds(locationIds)
        subscribeToViewModel()
    }

    override val adapter by lazy { LocationPagingAdapter() }

    override val itemDecoration by lazy {
        StyledItemDecoration(
            context = requireContext(),
            backgroundColor = requireContext().getColorAttr(
                com.google.android.material.R.attr.colorSecondary
            ),
            marginDp = 8f,
            cornerRadiusDp = 32f
        )
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            launch {
                viewModel.locationPagingData.collect { onPagingDataChanged(it) }
            }
            launch {
                viewModel.networkState.collect { state ->
                    onNetworkStateChanged(state) // RunOnUiThread?
                }
            }
        }
    }

    override fun onItemClicked(item: LocationPresentationEntity) {
        val fragment = LocationDetailsFragment().apply {
            arguments = bundleOf(LocationDetailsFragment.ARG_LOCATION_ID to item.id)
        }
        openDetailsFragment(fragment)
    }

    private suspend fun onPagingDataChanged(data: PagingData<LocationPresentationEntity>) {
        adapter.submitData(data)
        binding.content.refresher.isRefreshing = false
    }

    private fun onNetworkStateChanged(state: ListNetworkState) {
        binding.networkStatus.layoutNetworkLost.root.isVisible =
            state == ListNetworkState.Lost
        binding.networkStatus.layoutNetworkRestored.root.isVisible =
            state == ListNetworkState.Restored
    }

    override fun onRetryAfterError() {
        viewModel.refreshPagingData()
    }

    override fun onRefresherActivated() {
        viewModel.refreshPagingData()
    }

    override fun onNetworkAvailable() {
        viewModel.onNetworkAvailable()
    }

    override fun onNetworkLost() {
        viewModel.onNetworkLost()
    }
}