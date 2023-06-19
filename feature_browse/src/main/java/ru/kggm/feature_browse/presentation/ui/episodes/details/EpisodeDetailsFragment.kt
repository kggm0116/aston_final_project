package ru.kggm.feature_browse.presentation.ui.episodes.details

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.fragments.fragment.ViewModelFragment
import ru.kggm.feature_main.databinding.FragmentEpisodeDetailsBinding
import ru.kggm.feature_browse.di.EpisodeComponent
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_main.R

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

    override fun getViewModelOwner() = this

    private val episodeId by lazy {
        requireNotNull(arguments?.getLong(ARG_EPISODE_ID)) { "Could not retrieve episode id" }
    }

    override fun onInitialize() {
        viewModel.loadEpisode(episodeId)
        subscribeToViewModel()
        initializeToolbar()
    }

    private fun initializeToolbar() {
        binding.toolbarEpisodeDetails.apply {
            setNavigationIcon(ru.kggm.presentation.R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { navigateBack() }
            menu.clear()
        }
    }

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.episode.collect { episode ->
                episode?.let { displayEpisode(it) }
            }
        }
    }

    private fun displayEpisode(episode: EpisodePresentationEntity) {
        with(binding.layoutEpisodeDetails) {
            binding.toolbarEpisodeDetails.title = episode.name
            layoutEpisodeDetailsTexts.textViewEpisodeCode.text = episode.code
            layoutEpisodeDetailsTexts.textViewEpisodeAirDate.text = requireContext().getString(
                R.string.composite_text_episode_air_date,
                episode.airDate
            )
        }
    }

    private fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    override fun onBackButtonPressed() {
        navigateBack()
    }
}