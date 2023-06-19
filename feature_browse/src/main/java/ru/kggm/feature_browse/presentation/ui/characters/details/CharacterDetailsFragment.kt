package ru.kggm.feature_browse.presentation.ui.characters.details

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.fragment.ViewModelFragment
import ru.kggm.feature_main.databinding.FragmentCharacterDetailsBinding
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListFragment
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListViewModel
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.NewFragmentCharacterDetailsBinding

class CharacterDetailsFragment :
    ViewModelFragment<NewFragmentCharacterDetailsBinding, CharacterDetailsViewModel>(
        CharacterDetailsViewModel::class.java,
) {

    companion object {
        const val ARG_CHARACTER_ID = "ARG_CHARACTER_ID"
    }

    override fun createBinding() = NewFragmentCharacterDetailsBinding.inflate(layoutInflater)
    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        CharacterComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun viewModelOwner() = this

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
                character?.let {
                    displayCharacter(it)
                    val episodeFragment = EpisodeListFragment().apply {
                        arguments = bundleOf(EpisodeListFragment.ARG_EPISODE_IDS to it.episodeIds)
                    }
                    childFragmentManager.commit {
                        replace(R.id.fragment_container_character_episodes, episodeFragment)
                    }
                }
            }
        }
    }

    private fun displayCharacter(character: CharacterPresentationEntity) {
        with (binding.newLayoutCharacterInfoFull) {
//            binding.toolbarCharacterDetails.title = character.name
            binding.imageCharacter.load(character.image) { crossfade(true) }

            textViewCharacterType.text = requireContext().getString(
                R.string.composite_text_character_type,
                character.type
            )
            textViewCharacterSpecies.text =
                requireContext().getString(
                    R.string.composite_text_character_species,
                    character.species
                )
            textViewCharacterStatus.text =
                requireContext().getString(
                    R.string.composite_text_character_status,
                    character.status.toResourceString(requireContext())
                )
            textViewCharacterGender.text =
                requireContext().getString(
                    R.string.composite_text_character_gender,
                    character.gender.toResourceString(requireContext())
                )

            textViewCharacterType.isVisible =
                character.type.isNotEmpty()
            textViewCharacterSpecies.isVisible =
                character.species.isNotEmpty()
        }
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    override val onBackButtonPressed = {
        navigateBack()
    }
}