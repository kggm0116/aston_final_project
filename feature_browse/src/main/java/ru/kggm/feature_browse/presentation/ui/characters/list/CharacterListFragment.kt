package ru.kggm.feature_browse.presentation.ui.characters.list

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
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
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.filter.CharacterFilterFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharacterPagingAdapter
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentCharacterListBinding
import ru.kggm.presentation.R as coreR

class CharacterListFragment :
    ViewModelFragment<FragmentCharacterListBinding, CharacterListViewModel>(
        CharacterListViewModel::class.java,
    ) {
    companion object {
        const val SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT = 20
    }

    override fun createBinding() = FragmentCharacterListBinding.inflate(layoutInflater)
    override fun getViewModelOwner() = requireActivity()

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeRecycler()
        initializeViews()
        initializeViewListeners()
        subscribeToViewModel()
        initializeNetworkListeners()
    }

    private val adapter by lazy { CharacterPagingAdapter() }
    private lateinit var layoutManager: FooterOptimizedGridLayoutManager
    private fun initializeRecycler() {
        layoutManager = FooterOptimizedGridLayoutManager(requireContext(), 2, adapter)
        binding.recyclerCharacters.layoutManager = layoutManager
        binding.recyclerCharacters.adapter =
            adapter.withLoadStateFooter(CommonLoadStateAdapter { adapter.retry() })

        adapter.onCharacterClicked = { onCharacterClicked(it) }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { displayLoadStates(it) }
        }
    }

    private fun displayLoadStates(states: CombinedLoadStates) {
        with(states) {
            binding.recyclerCharacters.animateVisibility(
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
        binding.fabOpenCharacterFilters.animateVisibility(
            Visibility.Visible,
            coreR.anim.slide_in_right
        )
    }

    private fun initializeViewListeners() {
        with (binding) {
            fabOpenCharacterFilters.setDebouncedClickListener {
                CharacterFilterFragment(
                    onClosed = {
                        fabOpenCharacterFilters.animateVisibility(
                            Visibility.Visible,
                            coreR.anim.slide_in_right
                        )
                    }
                ).show(parentFragmentManager, null)
                fabOpenCharacterFilters.animateVisibility(
                    Visibility.Gone,
                    coreR.anim.slide_out_right
                )
            }
            recyclerCharacters.addOnScrollListener(scrollListener)
            fabCharactersScrollToTop.setDebouncedClickListener {
                recyclerCharacters.stopScroll()
                layoutManager.scrollToPositionWithOffset(0, 0)
            }
            layoutPagerError.buttonPagerRetry.setDebouncedClickListener {
                adapter.retry()
            }
            refresherCharacters.setOnRefreshListener { onRefreshRecycler() }
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            launch {
                viewModel.characterPagingData.collect {
                    adapter.submitData(it)
                    binding.refresherCharacters.isRefreshing = false
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
            binding.fabCharactersScrollToTop.animateVisibility(
                Visibility.Visible,
                coreR.anim.slide_in_right
            )
        } else {
            binding.fabCharactersScrollToTop.animateVisibility(
                Visibility.Gone,
                coreR.anim.slide_out_right
            )
        }
    }

    private fun setNetworkLayoutsVisibility(state: CharacterListViewModel.NetworkState) {
        binding.layoutNetworkLost.root.isVisible =
            state == CharacterListViewModel.NetworkState.Lost
        binding.layoutNetworkRestored.root.isVisible =
            state == CharacterListViewModel.NetworkState.Restored
    }

    private fun animateNetworkLayoutsVisibility(state: CharacterListViewModel.NetworkState) {
        if (state == CharacterListViewModel.NetworkState.Lost) {
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
        if (state == CharacterListViewModel.NetworkState.Restored) {
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

    private fun onCharacterClicked(character: CharacterPresentationEntity) {
        val fragment = CharacterDetailsFragment().apply {
            arguments = bundleOf(CharacterDetailsFragment.ARG_CHARACTER_ID to character.id)
        }
        parentFragmentManager.commit {
            setCustomAnimations(
                coreR.anim.slide_in_right,
                coreR.anim.slide_out_left,
                coreR.anim.slide_in_left,
                coreR.anim.slide_out_right
            )
            add(R.id.fragment_container_characters, fragment)
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