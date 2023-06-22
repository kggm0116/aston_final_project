package ru.kggm.feature_browse.presentation.ui.shared

import android.net.ConnectivityManager
import android.net.Network
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlinx.coroutines.launch
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.core.presentation.ui.paging.CommonLoadStateAdapter
import ru.kggm.core.presentation.ui.paging.FooterOptimizedGridLayoutManager
import ru.kggm.core.presentation.utility.network.getIsNetworkConnectionActive
import ru.kggm.core.presentation.utility.network.registerNetworkCallback
import ru.kggm.core.presentation.utility.network.unregisterNetworkCallback
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.ui.characters.filter.CharacterFilterFragment
import ru.kggm.feature_main.databinding.LayoutListBinding

abstract class BaseListFragment<VM, TAdapter, TData>(
    viewModelClass: Class<VM>
) : ViewModelFragment<LayoutListBinding, VM>(viewModelClass)
        where VM : ViewModel,
              TData : Any,
              TAdapter : PagingDataAdapter<TData, *>,
              TAdapter : ItemClickable<TData> {

    companion object {
        const val SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT = 10
    }

    abstract val filterEnabled: Boolean

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkCallback(networkCallback)
    }

    override fun onInitialize() {
        super.onInitialize()
        initializeRecycler()
        initializeViewListeners()
        initializeNetworkListeners()
    }

    protected open val columnCount = 2
    protected abstract val adapter: TAdapter
    protected open val itemDecoration: ItemDecoration? = null
    private lateinit var layoutManager: FooterOptimizedGridLayoutManager
    abstract fun onItemClicked(item: TData)
    private fun initializeRecycler() {
        layoutManager = FooterOptimizedGridLayoutManager(requireContext(), columnCount, adapter)
        binding.content.recycler.layoutManager = layoutManager
        binding.content.recycler.adapter =
            adapter.withLoadStateFooter(CommonLoadStateAdapter { adapter.retry() })
        itemDecoration?.let {
            binding.content.recycler.addItemDecoration(it)
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { displayLoadStates(it) }
        }
        adapter.onItemClicked = { onItemClicked(it) }
    }

    private fun initializeViewListeners() {
        initializeFilter()
        initializeScrollToTop()
        binding.content.layoutError.buttonRetry.setDebouncedClickListener { onRetryAfterError() }
        binding.content.refresher.setOnRefreshListener { onRefresherActivated() }
    }

    private fun initializeFilter() {
        if (filterEnabled) {
            binding.overlay.fabOpenFilters.isVisible = false
        } else {
            binding.overlay.fabOpenFilters.setDebouncedClickListener { onFilterFabClick() }
        }
    }

    private fun initializeScrollToTop() {
        binding.content.recycler.addOnScrollListener(recyclerScrollListener)
        binding.overlay.fabScrollToTop.isVisible = false
        binding.overlay.fabScrollToTop.setDebouncedClickListener { onScrollToTop() }
    }

    private val recyclerScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            binding.overlay.fabScrollToTop.isVisible =
                layoutManager.findFirstVisibleItemPosition() >= SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT
        }
    }

    private fun onScrollToTop() {
        binding.content.recycler.stopScroll()
        layoutManager.scrollToPositionWithOffset(0, 0)
    }

    protected abstract fun onRetryAfterError()

    private fun onFilterFabClick() {
        binding.overlay.fabOpenFilters.isVisible = false
        CharacterFilterFragment(
            onClosed = {
                binding.overlay.fabOpenFilters.isVisible = true
            }
        ).show(parentFragmentManager, null)
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

    private fun initializeNetworkListeners() {
        if (!getIsNetworkConnectionActive())
            onNetworkLost()
        registerNetworkCallback(networkCallback)
    }

    protected abstract fun onRefresherActivated()
    protected abstract fun onNetworkAvailable()
    protected abstract fun onNetworkLost()

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
}