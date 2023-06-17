package ru.kggm.feature_browse.presentation.ui.characters.list

import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.fragment.ViewModelFragment
import ru.kggm.feature_main.R
import ru.kggm.presentation.R as coreR
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.core.presentation.ui.paging.FooterOptimizedGridLayoutManager
import ru.kggm.core.presentation.ui.paging.CommonLoadStateAdapter
import ru.kggm.core.presentation.utility.animations.Visibility
import ru.kggm.core.presentation.utility.animations.animateVisibility
import ru.kggm.feature_browse.presentation.ui.characters.list.filter.CharacterFilterFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharacterPagingAdapter
import ru.kggm.feature_main.databinding.FragmentCharacterListBinding

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
        binding.refresherCharacters.setOnRefreshListener { onRefreshRecycler() }
        lifecycleScope.launch {
            subscribeToViewModel()
        }
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
        binding.fabOpenCharacterFilters.setOnClickListener {
            CharacterFilterFragment(
                onCancel = {
                    binding.fabOpenCharacterFilters.animateVisibility(
                        Visibility.Visible,
                        coreR.anim.slide_in_right
                    )
                }
            ).show(parentFragmentManager, null)
            binding.fabOpenCharacterFilters.animateVisibility(
                Visibility.Gone,
                coreR.anim.slide_out_right
            )
        }
        binding.recyclerCharacters.addOnScrollListener(scrollListener)
        binding.fabCharactersScrollToTop.setOnClickListener {
            binding.recyclerCharacters.stopScroll()
            layoutManager.scrollToPositionWithOffset(0, 0)
        }
        binding.layoutPagerError.buttonPagerRetry.setOnClickListener {
            adapter.retry()
        }
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

    private suspend fun subscribeToViewModel() {
        viewModel.characterPagingData.collect {
            adapter.submitData(it)
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
        binding.refresherCharacters.isRefreshing = false
    }
}