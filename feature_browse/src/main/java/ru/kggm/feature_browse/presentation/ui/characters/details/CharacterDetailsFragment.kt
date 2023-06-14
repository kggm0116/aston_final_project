package ru.kggm.feature_browse.presentation.ui.characters.details

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.ViewModelFragment
import ru.kggm.core.presentation.utility.parentFragmentOfType
import ru.kggm.feature_main.databinding.FragmentCharacterDetailsBinding
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.CharactersFragment
import ru.kggm.feature_browse.presentation.ui.characters.CharactersViewModel

class CharacterDetailsFragment :
    ViewModelFragment<FragmentCharacterDetailsBinding, CharactersViewModel>(
        CharactersViewModel::class.java,
    ) {
    override fun createBinding() = FragmentCharacterDetailsBinding.inflate(layoutInflater)
    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun getViewModelOwner() = parentFragmentOfType<CharactersFragment>()

    override fun onInitialize() {
        lifecycleScope.launch { subscribeToViewModel() }
    }

    private suspend fun subscribeToViewModel() {
        viewModel.detailedCharacter.collect { character ->
            character?.let { display(it) }
        }
    }

    private fun display(character: CharacterPresentationEntity) {
        with(character) {
            binding.textViewCharacterName.text = name
        }
    }
}