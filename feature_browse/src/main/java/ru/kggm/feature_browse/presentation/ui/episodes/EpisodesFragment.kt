package ru.kggm.feature_browse.presentation.ui.episodes

import androidx.fragment.app.commit
import ru.kggm.core.presentation.ui.fragments.fragment.ViewBindingFragment
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListFragment
import ru.kggm.feature_browse.presentation.ui.locations.list.LocationListFragment
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentCharactersBinding
import ru.kggm.feature_main.databinding.FragmentEpisodesBinding

class EpisodesFragment : ViewBindingFragment<FragmentEpisodesBinding>() {
    override fun createBinding() = FragmentEpisodesBinding.inflate(layoutInflater)

    override fun onInitialize() {

    }
}