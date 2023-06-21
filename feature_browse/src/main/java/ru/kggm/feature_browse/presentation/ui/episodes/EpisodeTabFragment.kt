package ru.kggm.feature_browse.presentation.ui.episodes

import ru.kggm.core.presentation.ui.fragments.base.ViewBindingFragment
import ru.kggm.feature_main.databinding.FragmentEpisodeTabBinding

class EpisodeTabFragment : ViewBindingFragment<FragmentEpisodeTabBinding>() {
    override fun createBinding() = FragmentEpisodeTabBinding.inflate(layoutInflater)
}