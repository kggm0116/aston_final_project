package ru.kggm.feature_browse.presentation.ui.episodes.list

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.recycler.StyledItemDecoration
import ru.kggm.core.presentation.utility.getColorAttr
import ru.kggm.feature_browse.di.EpisodeComponent
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.presentation.ui.episodes.details.EpisodeDetailsFragment
import ru.kggm.feature_browse.presentation.ui.episodes.recycler.EpisodePagingAdapter
import ru.kggm.feature_browse.presentation.ui.shared.BaseListFragment
import ru.kggm.feature_browse.presentation.ui.shared.ListNetworkState
import ru.kggm.feature_browse.presentation.ui.shared.openDetailsFragment
import ru.kggm.feature_main.databinding.LayoutListBinding

class EpisodeListFragment :
    BaseListFragment<EpisodeListViewModel, EpisodePagingAdapter, EpisodePresentationEntity>(
        EpisodeListViewModel::class.java,
    ) {
    companion object {
        const val ARG_EPISODE_IDS = "ARG_EPISODE_IDS"
    }

    private val episodeIds by lazy {
        arguments?.getIntegerArrayList(ARG_EPISODE_IDS)?.toList()
    }

    override val filterEnabled by lazy { episodeIds != null }

    override fun createBinding() = LayoutListBinding.inflate(layoutInflater)
    override fun viewModelOwner(): ViewModelStoreOwner = if (episodeIds != null) {
        requireParentFragment()
    } else {
        requireActivity()
    }

    override val columnCount by lazy {
        if (episodeIds != null) {
            1
        } else {
            2
        }
    }

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        EpisodeComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        super.onInitialize()
        viewModel.setIds(episodeIds)
        subscribeToViewModel()
    }

    override val adapter by lazy { EpisodePagingAdapter() }

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
                viewModel.episodePagingData.collect { onPagingDataChanged(it) }
            }
            launch {
                viewModel.networkState.collect { state ->
                    onNetworkStateChanged(state) // RunOnUiThread?
                }
            }
        }
    }

    override fun onItemClicked(item: EpisodePresentationEntity) {
        val fragment = EpisodeDetailsFragment().apply {
            arguments = bundleOf(EpisodeDetailsFragment.ARG_EPISODE_ID to item.id)
        }
        openDetailsFragment(fragment)
    }

    private suspend fun onPagingDataChanged(data: PagingData<EpisodePresentationEntity>) {
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