package ru.kggm.feature_browse.presentation.ui.characters.details

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.ViewModelFragment
import ru.kggm.core.presentation.utility.parentFragmentOfType
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_main.databinding.FragmentCharacterDetailsBinding
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.CharactersFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListViewModel

class CharacterDetailsFragment :
    ViewModelFragment<FragmentCharacterDetailsBinding, CharacterDetailsViewModel>(
        CharacterDetailsViewModel::class.java,
    ) {

    companion object {
        const val ARG_CHARACTER_ID = "ARG_CHARACTER_ID"
    }

    override fun createBinding() = FragmentCharacterDetailsBinding.inflate(layoutInflater)
    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun getViewModelOwner() = this

    private val characterId by lazy {
        requireNotNull(arguments?.getLong(ARG_CHARACTER_ID)) { "Could not retrieve character id" }
    }

    override fun onInitialize() {
        viewModel.loadCharacter(characterId)
        lifecycleScope.launch { subscribeToViewModel() }
        initializeViewObservers()
    }

    private suspend fun subscribeToViewModel() {
        viewModel.character.collect { character ->
            character?.let { display(it) }
        }
    }

    private fun initializeViewObservers() {
        binding.fabBack.setDebouncedClickListener { navigateBack() }
    }

    private fun display(character: CharacterPresentationEntity) {
        with(character) {
            binding.textViewCharacterName.text = name
        }
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }
}