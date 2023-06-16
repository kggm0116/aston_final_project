package ru.kggm.feature_browse.presentation.ui.characters.list

import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.ViewModelFragment
import ru.kggm.core.presentation.utility.parentFragmentOfType
import ru.kggm.feature_main.R
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.CharactersFragment
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharacterLayoutManager
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharacterLoadStateAdapter
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
        initializeViewListeners()
        binding.refresherCharacters.setOnRefreshListener { onRefreshRecycler() }
        lifecycleScope.launch {
            subscribeToViewModel()
        }
    }

    private val adapter by lazy { CharacterPagingAdapter() }
    private lateinit var layoutManager: CharacterLayoutManager
    private fun initializeRecycler() {
        layoutManager = CharacterLayoutManager(requireContext(), 2, adapter)
        binding.recyclerCharacters.layoutManager = layoutManager
        binding.recyclerCharacters.adapter = adapter
            .withLoadStateFooter(CharacterLoadStateAdapter())
        adapter.onCharacterClicked = { onCharacterClicked(it) }
    }

    private fun initializeViewListeners() {
        binding.fabOpenCharacterFilters.setOnClickListener {
            CharacterFilterFragment().show(parentFragmentManager, "bottom sheet dialog")
        }
        binding.recyclerCharacters.addOnScrollListener(scrollListener)
        binding.fabCharactersScrollToTop.setOnClickListener {
            binding.recyclerCharacters.stopScroll()
            layoutManager.scrollToPositionWithOffset(0, 0)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            binding.fabCharactersScrollToTop.isVisible =
                layoutManager.findFirstVisibleItemPosition() >= SCROLL_TO_TOP_VISIBILITY_ITEM_COUNT
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
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
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