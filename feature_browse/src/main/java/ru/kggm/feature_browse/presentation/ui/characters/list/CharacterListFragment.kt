package ru.kggm.feature_browse.presentation.ui.characters.list

import android.graphics.Color
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
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.core.presentation.ui.paging.CommonLoadStateAdapter
import ru.kggm.core.presentation.ui.paging.FooterOptimizedGridLayoutManager
import ru.kggm.core.presentation.utility.network.getIsNetworkConnectionActive
import ru.kggm.core.presentation.utility.network.registerNetworkCallback
import ru.kggm.core.presentation.utility.network.unregisterNetworkCallback
import ru.kggm.core.presentation.utility.runOnUiThread
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.filter.CharacterFilterFragment
import ru.kggm.core.presentation.ui.recycler.GridItemDecoration
import ru.kggm.core.presentation.utility.getColorAttr
import ru.kggm.feature_browse.presentation.ui.characters.recycler.CharacterPagingAdapter
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.LayoutListBinding
import ru.kggm.presentation.R as coreR

class CharacterListFragment :
    ViewModelFragment<LayoutListBinding, CharacterListViewModel>(
        CharacterListViewModel::class.java,
    ) {
    companion object {
        const val SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT = 10
        const val ARG_CHARACTER_IDS = "ARG_CHARACTER_IDS"
    }

    private val characterIds by lazy {
        arguments?.getIntArray(ARG_CHARACTER_IDS)?.toList()
    }

    private val showsLimitedIds get() = characterIds != null

    override fun createBinding() = LayoutListBinding.inflate(layoutInflater)
    override fun viewModelOwner(): ViewModelStoreOwner = if (showsLimitedIds) {
        requireParentFragment()
    } else {
        requireActivity()
    }

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        viewModel.setIds(characterIds)
        initializeRecycler()
        initializeViewListeners()
        subscribeToViewModel()
        initializeNetworkListeners()
    }

    private val adapter by lazy { CharacterPagingAdapter() }
    private lateinit var layoutManager: FooterOptimizedGridLayoutManager
    private fun initializeRecycler() {
        layoutManager = FooterOptimizedGridLayoutManager(requireContext(), 2, adapter)
        binding.content.recycler.layoutManager = layoutManager
        binding.content.recycler.adapter =
            adapter.withLoadStateFooter(CommonLoadStateAdapter { adapter.retry() })
        binding.content.recycler.addItemDecoration(itemDecoration)
        adapter.onCharacterClicked = { onCharacterClicked(it) }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { displayLoadStates(it) }
        }
    }

    private val itemDecoration by lazy {
        GridItemDecoration(
            context = requireContext(),
            backgrounColor = requireContext().getColorAttr(
                com.google.android.material.R.attr.colorSecondary
            ),
            marginDp = 16f,
            cornerRadiusDp = 32f
        )
    }

    private fun displayLoadStates(states: CombinedLoadStates) {
        binding.content.recycler.isVisible = adapter.itemCount > 0
        binding.content.layoutEmpty.root.isVisible =
            adapter.itemCount == 0 && states.refresh is LoadState.NotLoading
        binding.content.layoutLoading.root.isVisible =
            adapter.itemCount == 0 && states.refresh is LoadState.Loading
        binding.content.layoutError.root.isVisible =
            adapter.itemCount == 0 && states.refresh is LoadState.Error
    }

    private fun initializeViewListeners() {
        initializeFilter()
        binding.content.recycler.addOnScrollListener(scrollListener)
        binding.overlay.fabScrollToTop.setDebouncedClickListener {
            binding.content.recycler.stopScroll()
            layoutManager.scrollToPositionWithOffset(0, 0)
        }
        binding.content.layoutError.buttonRetry.setDebouncedClickListener {
//            adapter.retry()
            viewModel.refreshPagingData()
        }
        binding.content.refresher.setOnRefreshListener { onRefreshRecycler() }
    }


    private fun initializeFilter() {
        if (showsLimitedIds) {
            binding.overlay.fabOpenFilters.isVisible = false
        } else {
            binding.overlay.fabOpenFilters.setDebouncedClickListener {
                binding.overlay.fabOpenFilters.isVisible = false
                CharacterFilterFragment(
                    onClosed = {
                        binding.overlay.fabOpenFilters.isVisible = true
                    }
                ).show(parentFragmentManager, null)
            }
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            launch {
                viewModel.characterPagingData.collect {
                    adapter.submitData(it)
                    binding.content.refresher.isRefreshing = false
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
        try {
            unregisterNetworkCallback(networkCallback)
        } catch (_: Throwable) {
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            animateScrollToTopFab()
        }
    }

    private fun animateScrollToTopFab() {
        binding.overlay.fabScrollToTop.isVisible =
            layoutManager.findFirstVisibleItemPosition() >= SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT
    }

    private fun setNetworkLayoutsVisibility(state: CharacterListViewModel.NetworkState) {
        binding.networkStatus.layoutNetworkLost.root.isVisible =
            state == CharacterListViewModel.NetworkState.Lost
        binding.networkStatus.layoutNetworkRestored.root.isVisible =
            state == CharacterListViewModel.NetworkState.Restored
    }

    private fun animateNetworkLayoutsVisibility(state: CharacterListViewModel.NetworkState) {
        binding.networkStatus.layoutNetworkLost.root.isVisible =
            state == CharacterListViewModel.NetworkState.Lost
        binding.networkStatus.layoutNetworkRestored.root.isVisible =
            state == CharacterListViewModel.NetworkState.Restored
    }

    private fun onCharacterClicked(character: CharacterPresentationEntity) {
        val fragment = CharacterDetailsFragment().apply {
            arguments = bundleOf(CharacterDetailsFragment.ARG_CHARACTER_ID to character.id)
        }
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(
                coreR.anim.slide_in_right,
                coreR.anim.slide_out_left,
                coreR.anim.slide_in_left,
                coreR.anim.slide_out_right
            )
            replace(R.id.fragment_container_browse, fragment)
            addToBackStack(null)
        }
    }

    private fun onRefreshRecycler() {
        viewModel.refreshPagingData()
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