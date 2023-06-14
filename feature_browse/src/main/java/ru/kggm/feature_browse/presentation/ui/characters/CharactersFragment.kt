package ru.kggm.feature_browse.presentation.ui.characters

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.ui.ViewBindingFragment
import ru.kggm.core.presentation.ui.ViewModelFragment
import ru.kggm.feature_main.R
import ru.kggm.feature_main.databinding.FragmentCharactersBinding
import ru.kggm.feature_browse.di.CharacterComponent
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.recycler.CharactersListAdapter

class CharactersFragment : ViewBindingFragment<FragmentCharactersBinding>() {
    override fun createBinding() = FragmentCharactersBinding.inflate(layoutInflater)

    override fun onInitialize() {

    }
}