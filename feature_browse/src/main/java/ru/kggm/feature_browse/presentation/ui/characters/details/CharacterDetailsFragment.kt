package ru.kggm.feature_browse.presentation.ui.characters.details

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.fragment.ViewModelFragment
import ru.kggm.feature_main.databinding.FragmentCharacterDetailsBinding
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_main.R

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
        subscribeToViewModel()
        initializeToolbar()
    }

    private fun initializeToolbar() {
        binding.toolbarCharacterDetails.apply {
            setNavigationIcon(ru.kggm.presentation.R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { navigateBack() }
            menu.clear()
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.character.collect { character ->
                character?.let { displayCharacter(it) }
            }
        }
    }

    private fun displayCharacter(character: CharacterPresentationEntity) {
        with(binding.layoutCharacterDetails) {
            binding.toolbarCharacterDetails.title = character.name
            imageCharacter.load(character.image) { crossfade(true) }

            layoutCharacterDetailsTexts.textViewCharacterType.text = requireContext().getString(
                R.string.composite_text_character_type,
                character.type
            )
            layoutCharacterDetailsTexts.textViewCharacterSpecies.text =
                requireContext().getString(
                    R.string.composite_text_character_species,
                    character.species
                )
            layoutCharacterDetailsTexts.textViewCharacterStatus.text =
                requireContext().getString(
                    R.string.composite_text_character_status,
                    character.status.toResourceString(requireContext())
                )
            layoutCharacterDetailsTexts.textViewCharacterGender.text =
                requireContext().getString(
                    R.string.composite_text_character_gender,
                    character.gender.toResourceString(requireContext())
                )

            layoutCharacterDetailsTexts.textViewCharacterType.isVisible =
                character.type.isNotEmpty()
            layoutCharacterDetailsTexts.textViewCharacterSpecies.isVisible =
                character.species.isNotEmpty()
        }
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    override fun onBackButtonPressed() {
        navigateBack()
    }
}