package ru.kggm.feature_browse.presentation.ui.locations.list

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.fragment.ViewModelFragment
import ru.kggm.core.presentation.ui.paging.CommonLoadStateAdapter
import ru.kggm.core.presentation.ui.paging.FooterOptimizedGridLayoutManager
import ru.kggm.core.presentation.utility.animations.Visibility
import ru.kggm.core.presentation.utility.animations.animateVisibility
import ru.kggm.core.presentation.utility.network.getIsNetworkConnectionActive
import ru.kggm.core.presentation.utility.network.registerNetworkCallback
import ru.kggm.core.presentation.utility.network.unregisterNetworkCallback
import ru.kggm.core.presentation.utility.runOnUiThread
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.di.LocationComponent
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.locations.details.LocationDetailsFragment
import ru.kggm.feature_browse.presentation.ui.locations.list.filter.LocationFilterFragment
import ru.kggm.feature_browse.presentation.ui.locations.recycler.LocationPagingAdapter
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentCharacterListBinding
import ru.kggm.feature_main.databinding.FragmentLocationListBinding
import ru.kggm.presentation.R as coreR

class LocationListFragment :
    ViewModelFragment<FragmentLocationListBinding, LocationListViewModel>(
        LocationListViewModel::class.java,
    ) {
    companion object {
        const val SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT = 10
        const val ARG_LOCATION_IDS = "ARG_LOCATION_IDS"
    }

    private val locationIds by lazy {
        arguments?.getIntArray(ARG_LOCATION_IDS)?.toList()
    }

    override fun createBinding() = FragmentLocationListBinding.inflate(layoutInflater)
    override fun viewModelOwner(): ViewModelStoreOwner = if (locationIds == null) {
        requireActivity()
    } else {
        requireParentFragment()
    }

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        LocationComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeRecycler()
        initializeViews()
        initializeViewListeners()
        subscribeToViewModel()
        initializeNetworkListeners()
    }

    private val adapter by lazy { LocationPagingAdapter() }
    private lateinit var layoutManager: FooterOptimizedGridLayoutManager
    private fun initializeRecycler() {
        layoutManager = FooterOptimizedGridLayoutManager(requireContext(), 2, adapter)
        binding.recyclerLocations.layoutManager = layoutManager
        binding.recyclerLocations.adapter =
            adapter.withLoadStateFooter(CommonLoadStateAdapter { adapter.retry() })

        adapter.onLocationClicked = { onLocationClicked(it) }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { displayLoadStates(it) }
        }
    }

    private fun displayLoadStates(states: CombinedLoadStates) {
        with(states) {
            binding.recyclerLocations.animateVisibility(
                visibility = if (adapter.itemCount > 0)
                    Visibility.Visible
                else
                    Visibility.Invisible
            )
            binding.layoutPagerEmpty.root.animateVisibility {
                adapter.itemCount == 0 && refresh is LoadState.NotLoading
            }
            binding.layoutPagerLoading.root.animateVisibility {
                adapter.itemCount == 0 && refresh is LoadState.Loading
            }
            binding.layoutPagerError.root.animateVisibility {
                adapter.itemCount == 0 && refresh is LoadState.Error
            }
        }
    }

    private fun initializeViews() {
        binding.fabOpenLocationFilters.animateVisibility(
            Visibility.Visible,
            coreR.anim.slide_in_right
        )
    }

    private fun initializeViewListeners() {
        with (binding) {
            fabOpenLocationFilters.setDebouncedClickListener {
                fabOpenLocationFilters.animateVisibility(
                    Visibility.Gone,
                    coreR.anim.slide_out_right
                )
                LocationFilterFragment(
                    onClosed = {
                        fabOpenLocationFilters.animateVisibility(
                            Visibility.Visible,
                            coreR.anim.slide_in_right
                        )
                    }
                ).show(parentFragmentManager, null)
            }
            recyclerLocations.addOnScrollListener(scrollListener)
            fabLocationsScrollToTop.setDebouncedClickListener {
                recyclerLocations.stopScroll()
                layoutManager.scrollToPositionWithOffset(0, 0)
            }
            layoutPagerError.buttonPagerRetry.setDebouncedClickListener {
                adapter.retry()
            }
            refresherLocations.setOnRefreshListener { onRefreshRecycler() }
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            launch {
                viewModel.locationPagingData.collect {
                    adapter.submitData(it)
                    binding.refresherLocations.isRefreshing = false
                }
            }
            launch {
                var isInitialNetworkStateSet = false
                viewModel.networkState.collect { state ->
                    runOnUiThread {
                        if (!isInitialNetworkStateSet) {
                            isInitialNetworkStateSet = true
                            setNetworkLayoutsVisibility(state)
                        } else {
                            animateNetworkLayoutsVisibility(state)
                        }
                    }
                }
            }
        }
    }

    private fun initializeNetworkListeners() {
        if (!getIsNetworkConnectionActive())
            viewModel.onNetworkLost()
        registerNetworkCallback(networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkCallback(networkCallback)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            animateScrollToTopFab()
        }
    }

    private fun animateScrollToTopFab() {
        if (layoutManager.findFirstVisibleItemPosition() >= SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT) {
            binding.fabLocationsScrollToTop.animateVisibility(
                Visibility.Visible,
                coreR.anim.slide_in_right
            )
        } else {
            binding.fabLocationsScrollToTop.animateVisibility(
                Visibility.Gone,
                coreR.anim.slide_out_right
            )
        }
    }

    private fun setNetworkLayoutsVisibility(state: LocationListViewModel.NetworkState) {
        binding.layoutNetworkLost.root.isVisible =
            state == LocationListViewModel.NetworkState.Lost
        binding.layoutNetworkRestored.root.isVisible =
            state == LocationListViewModel.NetworkState.Restored
    }

    private fun animateNetworkLayoutsVisibility(state: LocationListViewModel.NetworkState) {
        if (state == LocationListViewModel.NetworkState.Lost) {
            binding.layoutNetworkLost.root.animateVisibility(
                Visibility.Visible,
                coreR.anim.slide_in_top
            )
        } else {
            binding.layoutNetworkLost.root.animateVisibility(
                Visibility.Gone,
                coreR.anim.slide_out_top
            )
        }
        if (state == LocationListViewModel.NetworkState.Restored) {
            binding.layoutNetworkRestored.root.animateVisibility(
                Visibility.Visible,
                coreR.anim.slide_in_top
            )
        } else {
            binding.layoutNetworkRestored.root.animateVisibility(
                Visibility.Gone,
                coreR.anim.slide_out_top
            )
        }
    }

    private fun onLocationClicked(location: LocationPresentationEntity) {
        val fragment = LocationDetailsFragment().apply {
            arguments = bundleOf(LocationDetailsFragment.ARG_LOCATION_ID to location.id)
        }
        parentFragmentManager.commit {
            setCustomAnimations(
                coreR.anim.slide_in_right,
                coreR.anim.slide_out_left,
                coreR.anim.slide_in_left,
                coreR.anim.slide_out_right
            )
            add(R.id.fragment_container_locations, fragment)
            addToBackStack(null)
        }
    }

    private fun onRefreshRecycler() {
        viewModel.refreshPagingData()
        viewModel.onDataRefreshed()
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkAvailable()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onNetworkLost()
        }
    }

    private fun onNetworkAvailable() {
        Log.i(classTag(), "Network available")
        viewModel.onNetworkRestored()
    }

    private fun onNetworkLost() {
        Log.i(classTag(), "Network lost")
        viewModel.onNetworkLost()
    }
}