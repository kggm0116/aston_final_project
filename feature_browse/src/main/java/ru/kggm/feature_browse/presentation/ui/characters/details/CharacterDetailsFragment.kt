package ru.kggm.feature_browse.presentation.ui.characters.details

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.core.presentation.utility.toClickableSpan
import ru.kggm.core.utility.classTag
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListFragment
import ru.kggm.feature_browse.presentation.ui.utility.resources.toResourceString
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentCharacterDetailsBinding

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
        subscribeToViewModel()
        initializeToolbar()
    }

    private fun initializeToolbar() {
        binding.toolbar.apply {
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
                        replace(R.id.fragment_container_episodes, episodeFragment)
                        addToBackStack(null)
                    }
                }
            }
        }
    }

    private fun displayCharacter(character: CharacterPresentationEntity) {
        binding.toolbar.title = character.name
        binding.image.load(character.image) { crossfade(true) }

        binding.info.textViewLocation.movementMethod = LinkMovementMethod.getInstance()

        binding.info.textViewType.text = requireContext().getString(
            R.string.composite_text_character_type,
            character.type
        )
        binding.info.textViewSpecies.text = requireContext().getString(
            R.string.composite_text_character_species,
            character.species.toClickableSpan(
                styling = {
                    color = Color.RED
                    isUnderlineText = true
                },
                onClick = {
                    Log.i(classTag(), "Span clicked")
                }
            )
        )
        binding.info.textViewStatus.text = requireContext().getString(
            R.string.composite_text_character_status,
            character.status.toResourceString(requireContext())
        )
        binding.info.textViewGender.text = requireContext().getString(
            R.string.composite_text_character_gender,
            character.gender.toResourceString(requireContext())
        )

        binding.info.textViewType.isVisible =
            character.type.isNotEmpty()
        binding.info.textViewSpecies.isVisible =
            character.species.isNotEmpty()
    }

    private fun getLocationSpan() {
        val text = "Click here to learn more about "
        val clickableText = "Kotlin"
        val spannable = SpannableString(text + clickableText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Handle click here
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Set the color of the clickable text
                ds.isUnderlineText = false // Remove the underline from the clickable text
            }
        }
        spannable.setSpan(clickableSpan, text.length, text.length + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    override val onBackButtonPressed = {
        navigateBack()
    }
}