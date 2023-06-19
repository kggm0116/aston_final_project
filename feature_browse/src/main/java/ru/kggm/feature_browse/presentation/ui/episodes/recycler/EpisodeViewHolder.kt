package ru.kggm.feature_browse.presentation.ui.episodes.recycler

import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_main.databinding.PagerItemEpisodeBinding

class EpisodeViewHolder(
    private val binding: PagerItemEpisodeBinding,
    private val onEpisodeClicked: (EpisodePresentationEntity) -> Unit = { }
) : BaseViewHolder<PagerItemEpisodeBinding>(binding) {

    lateinit var episode: EpisodePresentationEntity

    fun bind(episode: EpisodePresentationEntity) {
        this.episode = episode
        binding.root.setDebouncedClickListener { onEpisodeClicked(episode) }
        displayEpisode()
    }
    
    private fun displayEpisode() {
        with (episode) {
            binding.textViewEpisodeName.text = name
            binding.textViewEpisodeCode.text = code
            binding.textViewEpisodeDate.text = airDate.toString()
        }
    }
}



