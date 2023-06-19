package ru.kggm.feature_browse.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kggm.core.presentation.di.ViewModelKey
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsViewModel
import ru.kggm.feature_browse.presentation.ui.episodes.details.EpisodeDetailsViewModel

@Module
interface EpisodeDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EpisodeDetailsViewModel::class)
    fun bindCharacterDetailsViewModel(viewModel: EpisodeDetailsViewModel): ViewModel
}