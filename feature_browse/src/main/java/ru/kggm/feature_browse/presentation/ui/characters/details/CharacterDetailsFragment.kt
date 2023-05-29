package ru.kggm.feature_browse.presentation.ui.characters.details

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.core.presentation.utility.clickableSpan
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListFragment
import ru.kggm.feature_browse.presentation.ui.locations.details.LocationDetailsFragment
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import ru.kggm.feature_browse.presentation.ui.shared.openDetailsFragment
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_browse.R
import ru.kggm.feature_browse.databinding.FragmentCharacterDetailsBinding

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

    override fun viewModelOwner() = this

    private val characterId by lazy {
        requireNotNull(arguments?.getLong(ARG_CHARACTER_ID)) { "Could not retrieve character id" }
    }

    override fun onInitialize() {
        viewModel.loadCharacter(characterId)
        initializeViews()
        initializeToolbar()
        subscribeToViewModel()
    }

    private fun initializeToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(ru.kggm.core.R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { navigateBack() }
            menu.clear()
        }
    }

    private fun initializeViews() {
        binding.content.info.textViewOrigin.movementMethod = LinkMovementMethod.getInstance()
        binding.content.info.textViewLocation.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            launch {
                viewModel.character.collect { onCharacterChanged(it) }
            }
            launch {
                viewModel.origin.collect { onOriginChanged(it) }
            }
            launch {
                viewModel.location.collect { onLocationChanged(it) }
            }
        }
    }

    private fun onCharacterChanged(result: LoadResult<CharacterPresentationEntity?>) {
        binding.content.root.isVisible = result.state == LoadingState.Loaded
        binding.layoutLoading.root.isVisible = result.state == LoadingState.Loading
        binding.layoutError.root.isVisible = result.state == LoadingState.Error
        result.item?.let {
            displayCharacter(it)
            initializeEpisodeList(it) // What happens on config change?
        }
    }

    private fun onOriginChanged(result: LoadResult<LocationPresentationEntity?>) {
        binding.content.info.textViewOrigin.text = if (result.item == null) {
            when (result.state) {
                LoadingState.Loaded -> requireContext().getString(R.string.text_info_none)
                LoadingState.Loading -> requireContext().getString(R.string.text_info_loading)
                LoadingState.Error -> requireContext().getString(R.string.text_info_loading_error)
            }.let { statusString ->
                requireContext().getString(
                    R.string.composite_text_character_origin,
                    statusString
                )
            }
        } else {
            getOriginText(result.item)
        }
    }

    private fun onLocationChanged(result: LoadResult<LocationPresentationEntity?>) {
        binding.content.info.textViewLocation.text = if (result.item == null) {
            when (result.state) {
                LoadingState.Loaded -> requireContext().getString(R.string.text_info_none)
                LoadingState.Loading -> requireContext().getString(R.string.text_info_loading)
                LoadingState.Error -> requireContext().getString(R.string.text_info_loading_error)
            }.let { statusString ->
                requireContext().getString(
                    R.string.composite_text_character_location,
                    statusString
                )
            }
        } else {
            getLocationText(result.item)
        }
    }

    private fun initializeEpisodeList(character: CharacterPresentationEntity) {
        val fragment = EpisodeListFragment().apply {
            arguments = bundleOf(
                EpisodeListFragment.ARG_EPISODE_IDS to character.episodeIds
            )
        }
        childFragmentManager.commit {
            replace(R.id.fragment_container_episodes, fragment)
            addToBackStack(null)
        }
    }

    private fun displayCharacter(character: CharacterPresentationEntity) {
        binding.toolbar.title = character.name
        binding.content.image.load(character.image) { crossfade(true) }

        binding.content.info.textViewLocation.movementMethod = LinkMovementMethod.getInstance()

        binding.content.info.textViewType.text = requireContext().getString(
            R.string.composite_text_character_type,
            character.type.ifEmpty { requireContext().getString(R.string.text_info_none) }
        )
        binding.content.info.textViewSpecies.text = requireContext().getString(
            R.string.composite_text_character_species,
            character.species.ifEmpty { requireContext().getString(R.string.text_info_none) }
        )
        binding.content.info.textViewStatus.text = requireContext().getString(
            R.string.composite_text_character_status,
            character.status.toResourceString(requireContext())
        )
        binding.content.info.textViewGender.text = requireContext().getString(
            R.string.composite_text_character_gender,
            character.gender.toResourceString(requireContext())
        )
    }

    private fun getOriginText(origin: LocationPresentationEntity): CharSequence {
        val plainText = requireContext().getString(
            R.string.text_character_origin
        )
        val span = clickableSpan(
            styling = {
                color = requireContext().getColor(R.color.color_light_blue)
                isUnderlineText = true
            },
            onClick = { onOriginClicked() }
        )
        return SpannableStringBuilder()
            .append("$plainText ")
            .append(origin.name, span, 0)
    }

    private fun getLocationText(origin: LocationPresentationEntity): CharSequence {
        val plainText = requireContext().getString(
            R.string.text_character_location
        )
        val span = clickableSpan(
            styling = {
                color = requireContext().getColor(R.color.color_light_blue)
                isUnderlineText = true
            },
            onClick = { onLocationClicked() }
        )
        return SpannableStringBuilder()
            .append("$plainText ")
            .append(origin.name, span, 0)
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    override val onBackButtonPressed = {
        navigateBack()
    }

    private fun onOriginClicked() {
        viewModel.origin.value.item?.id?.let { originId ->
            val fragment = LocationDetailsFragment().apply {
                arguments = bundleOf(LocationDetailsFragment.ARG_LOCATION_ID to originId)
            }
            openDetailsFragment(fragment)
        }
    }

    private fun onLocationClicked() {
        viewModel.location.value.item?.id?.let { locationId ->
            val fragment = LocationDetailsFragment().apply {
                arguments = bundleOf(LocationDetailsFragment.ARG_LOCATION_ID to locationId)
            }
            openDetailsFragment(fragment)
        }
    }
}