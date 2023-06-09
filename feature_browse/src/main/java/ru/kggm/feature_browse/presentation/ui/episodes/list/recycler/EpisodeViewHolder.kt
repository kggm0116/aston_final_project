package ru.kggm.feature_browse.presentation.ui.episodes.list.recycler

import ru.kggm.core.presentation.ui.recycler.BaseViewHolder
import ru.kggm.core.presentation.utility.setDebouncedClickListener
import ru.kggm.feature_browse.presentation.entities.EpisodePresentationEntity
import ru.kggm.feature_browse.R
import ru.kggm.feature_browse.databinding.LayoutEpisodeItemBinding

class EpisodeViewHolder(
    private val binding: LayoutEpisodeItemBinding,
    private val onEpisodeClicked: (EpisodePresentationEntity) -> Unit = { }
) : BaseViewHolder<LayoutEpisodeItemBinding>(binding) {

    lateinit var episode: EpisodePresentationEntity

    fun bind(episode: EpisodePresentationEntity) {
        this.episode = episode
        binding.root.setDebouncedClickListener { onEpisodeClicked(episode) }
        displayEpisode()
    }
    
    private fun displayEpisode() {
        with (episode) {
            binding.info.textViewEpisodeName.text = name
            binding.info.textViewCode.text = code
            binding.info.textViewAirDate.text = context.getString(
                R.string.composite_text_episode_air_date,
                airDate
            )
        }
    }
}



