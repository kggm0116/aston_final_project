package ru.kggm.feature_browse.presentation.ui.characters.list

import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.ViewModelFragment
import ru.kggm.core.presentation.utility.parentFragmentOfType
import ru.kggm.feature_main.R
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.CharactersFragment
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharactersListAdapter
import ru.kggm.feature_main.databinding.FragmentCharacterListBinding

class CharacterListFragment : ViewModelFragment<FragmentCharacterListBinding, CharacterListViewModel>(
    CharacterListViewModel::class.java,
) {
    override fun createBinding() = FragmentCharacterListBinding.inflate(layoutInflater)
    override fun getViewModelOwner() = parentFragmentOfType<CharactersFragment>()

    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun onInitialize() {
        initializeRecycler()
        initializeViewListeners()
        binding.refresherCharacters.setOnRefreshListener { onRefreshRecycler() }
        lifecycleScope.launch { subscribeToViewModel() }
    }

    private val recyclerAdapter = CharactersListAdapter()
    private fun initializeRecycler() {
        binding.recyclerCharacters.adapter = recyclerAdapter
        recyclerAdapter.onCharacterClicked = { onCharacterClicked(it) }
    }

    private fun initializeViewListeners() {
        binding.fabOpenCharacterFilters.setOnClickListener {
            CharacterFilterFragment().show(parentFragmentManager, "bottom sheet dialog")
        }
    }

    private suspend fun subscribeToViewModel() {
        viewModel.characterPagingData.collect {
            recyclerAdapter.submitData(it)
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