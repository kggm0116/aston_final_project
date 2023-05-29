package ru.kggm.feature_browse.presentation.ui.episodes.details

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.base.ViewModelFragment
import ru.kggm.feature_browse.databinding.FragmentEpisodeDetailsBinding
import ru.kggm.feature_browse.di.EpisodeComponent
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import ru.kggm.feature_browse.R

class EpisodeDetailsFragment :
    ViewModelFragment<FragmentEpisodeDetailsBinding, EpisodeDetailsViewModel>(
        EpisodeDetailsViewModel::class.java,
    ) {

    companion object {
        const val ARG_EPISODE_ID = "ARG_EPISODE_ID"
    }

    override fun createBinding() = FragmentEpisodeDetailsBinding.inflate(layoutInflater)
    override fun initDaggerComponent(dependenciesProvider: DependenciesProvider) {
        EpisodeComponent.init(requireContext(), dependenciesProvider).inject(this)
    }

    override fun viewModelOwner() = this

    private val episodeId by lazy {
        requireNotNull(arguments?.getLong(ARG_EPISODE_ID)) { "Could not retrieve episode id" }
    }

    override fun onInitialize() {
        viewModel.loadEpisode(episodeId)
        subscribeToViewModel()
        initializeToolbar()
    }

    private fun initializeToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(ru.kggm.core.R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { navigateBack() }
            menu.clear()
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.episode.collect { onEpisodeChanged(it) }
        }
    }

    private fun onEpisodeChanged(result: LoadResult<EpisodePresentationEntity?>) {
        binding.content.root.isVisible = result.state == LoadingState.Loaded
        binding.layoutLoading.root.isVisible = result.state == LoadingState.Loading
        binding.layoutError.root.isVisible = result.state == LoadingState.Error
        result.item?.let {
            displayEpisode(it)
            initializeCharacterList(it) // What happens on config change?
        }
    }

    private fun displayEpisode(episode: EpisodePresentationEntity) {
        binding.toolbar.title = episode.name
        binding.content.info.textViewCode.text = episode.code
        binding.content.info.textViewAirDate.text = requireContext().getString(
            R.string.composite_text_episode_air_date,
            episode.airDate
        )
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    private fun initializeCharacterList(episode: EpisodePresentationEntity) {
        val fragment = CharacterListFragment().apply {
            arguments = bundleOf(
                CharacterListFragment.ARG_CHARACTER_IDS to episode.characterIds
            )
        }
        childFragmentManager.commit {
            replace(R.id.fragment_container_featured_characters, fragment)
            addToBackStack(null)
        }
    }
}