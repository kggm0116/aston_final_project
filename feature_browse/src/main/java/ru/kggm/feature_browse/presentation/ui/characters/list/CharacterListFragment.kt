package ru.kggm.feature_browse.presentation.ui.characters.list

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.recycler.StyledItemDecoration
import ru.kggm.core.presentation.utility.getColorAttr
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharacterPagingAdapter
import ru.kggm.feature_browse.presentation.ui.shared.BaseListFragment
import ru.kggm.feature_browse.presentation.ui.shared.ListNetworkState
import ru.kggm.feature_browse.presentation.ui.shared.openDetailsFragment
import ru.kggm.feature_browse.databinding.LayoutListBinding
import ru.kggm.feature_browse.presentation.ui.characters.filter.CharacterFilterFragment

class CharacterListFragment :
    BaseListFragment<CharacterListViewModel, CharacterPagingAdapter, CharacterPresentationEntity>(
        CharacterListViewModel::class.java,
    ) {
    companion object {
        const val ARG_CHARACTER_IDS = "ARG_CHARACTER_IDS"
    }

    private val characterIds by lazy {
        arguments?.getIntegerArrayList(ARG_CHARACTER_IDS)?.toList()
    }

    override val filterEnabled by lazy { characterIds != null }

    override fun createBinding() = LayoutListBinding.inflate(layoutInflater)
    override fun viewModelOwner(): ViewModelStoreOwner = if (characterIds != null) {
        requireParentFragment()
    } else {
        requireActivity()
    }

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        super.onInitialize()
        viewModel.setIds(characterIds)
        subscribeToViewModel()
    }

    override val adapter by lazy { CharacterPagingAdapter() }

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
                viewModel.characterPagingData.collect { onPagingDataChanged(it) }
            }
            launch {
                viewModel.networkState.collect { state ->
                    onNetworkStateChanged(state) // RunOnUiThread?
                }
            }
        }
    }

    override fun onItemClicked(item: CharacterPresentationEntity) {
        val fragment = CharacterDetailsFragment().apply {
            arguments = bundleOf(CharacterDetailsFragment.ARG_CHARACTER_ID to item.id)
        }
        openDetailsFragment(fragment)
    }

    override fun onFilterClicked() {
        CharacterFilterFragment(
            onClosed = {
                binding.overlay.fabOpenFilters.isVisible = true
            }
        ).show(parentFragmentManager, null)
    }

    private suspend fun onPagingDataChanged(data: PagingData<CharacterPresentationEntity>) {
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