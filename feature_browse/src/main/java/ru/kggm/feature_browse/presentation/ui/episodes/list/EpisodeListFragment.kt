package ru.kggm.feature_browse.presentation.ui.episodes.list

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
import ru.kggm.feature_browse.di.EpisodeComponent
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.episodes.details.EpisodeDetailsFragment
import ru.kggm.feature_browse.presentation.ui.episodes.list.filter.EpisodeFilterFragment
import ru.kggm.feature_browse.presentation.ui.episodes.recycler.EpisodePagingAdapter
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentEpisodeListBinding
import ru.kggm.presentation.R as coreR

class EpisodeListFragment :
    ViewModelFragment<FragmentEpisodeListBinding, EpisodeListViewModel>(
        EpisodeListViewModel::class.java,
    ) {
    companion object {
        const val SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT = 10
        const val ARG_EPISODE_IDS = "ARG_EPISODE_IDS"
    }

    private val episodeIds by lazy {
        arguments?.getIntegerArrayList(ARG_EPISODE_IDS)?.toList()
    }

    private val showsLimitedIds get() = episodeIds != null

    override fun createBinding() = FragmentEpisodeListBinding.inflate(layoutInflater)
    override fun viewModelOwner(): ViewModelStoreOwner = if (showsLimitedIds) {
        requireParentFragment()
    } else {
        requireActivity()
    }

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        EpisodeComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        viewModel.setIds(episodeIds)
        initializeRecycler()
        initializeViewListeners()
        subscribeToViewModel()
        initializeNetworkListeners()
    }

    private val adapter by lazy { EpisodePagingAdapter() }
    private lateinit var layoutManager: FooterOptimizedGridLayoutManager
    private fun initializeRecycler() {
        layoutManager = FooterOptimizedGridLayoutManager(requireContext(), 2, adapter)
        binding.recyclerEpisodes.layoutManager = layoutManager
        binding.recyclerEpisodes.adapter = adapter
            .withLoadStateFooter(CommonLoadStateAdapter { adapter.retry() })

        adapter.onEpisodeClicked = { onEpisodeClicked(it) }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { displayLoadStates(it) }
        }
    }

    private fun displayLoadStates(states: CombinedLoadStates) {
        with(states) {
            binding.recyclerEpisodes.animateVisibility(
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

    private fun initializeViewListeners() {
        with (binding) {
            initializeFilter()
            recyclerEpisodes.addOnScrollListener(scrollListener)
            fabEpisodesScrollToTop.setDebouncedClickListener {
                recyclerEpisodes.stopScroll()
                layoutManager.scrollToPositionWithOffset(0, 0)
            }
            layoutPagerError.buttonPagerRetry.setDebouncedClickListener {
                adapter.retry()
            }
            refresherEpisodes.setOnRefreshListener { onRefreshRecycler() }
        }
    }

    private fun initializeFilter() {
        if (showsLimitedIds) {
            binding.fabOpenEpisodeFilters.isVisible = false
        } else {
            binding.fabOpenEpisodeFilters.setDebouncedClickListener {
                binding.fabOpenEpisodeFilters.animateVisibility(
                    Visibility.Gone,
                    coreR.anim.slide_out_right
                )
                EpisodeFilterFragment(
                    onClosed = {
                        binding.fabOpenEpisodeFilters.animateVisibility(
                            Visibility.Visible,
                            coreR.anim.slide_in_right
                        )
                    }
                ).show(parentFragmentManager, null)
            }
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            launch {
                viewModel.episodePagingData.collect {
                    adapter.submitData(it)
                    binding.refresherEpisodes.isRefreshing = false
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
            binding.fabEpisodesScrollToTop.animateVisibility(
                Visibility.Visible,
                coreR.anim.slide_in_right
            )
        } else {
            binding.fabEpisodesScrollToTop.animateVisibility(
                Visibility.Gone,
                coreR.anim.slide_out_right
            )
        }
    }

    private fun setNetworkLayoutsVisibility(state: EpisodeListViewModel.NetworkState) {
        binding.layoutNetworkLost.root.isVisible =
            state == EpisodeListViewModel.NetworkState.Lost
        binding.layoutNetworkRestored.root.isVisible =
            state == EpisodeListViewModel.NetworkState.Restored
    }

    private fun animateNetworkLayoutsVisibility(state: EpisodeListViewModel.NetworkState) {
        if (state == EpisodeListViewModel.NetworkState.Lost) {
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
        if (state == EpisodeListViewModel.NetworkState.Restored) {
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

    private fun onEpisodeClicked(episode: EpisodePresentationEntity) {
        val fragment = EpisodeDetailsFragment().apply {
            arguments = bundleOf(EpisodeDetailsFragment.ARG_EPISODE_ID to episode.id)
        }
        parentFragmentManager.commit {
            setCustomAnimations(
                coreR.anim.slide_in_right,
                coreR.anim.slide_out_left,
                coreR.anim.slide_in_left,
                coreR.anim.slide_out_right
            )
            add(R.id.fragment_container_episodes, fragment)
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