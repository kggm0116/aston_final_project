package ru.kggm.feature_browse.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kggm.core.presentation.di.ViewModelKey
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsViewModel
import ru.kggm.feature_browse.presentation.ui.episodes.details.EpisodeDetailsViewModel
import ru.kggm.feature_browse.presentation.ui.locations.details.LocationDetailsViewModel

@Module
interface LocationDetailsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationDetailsViewModel::class)
    fun bindCharacterDetailsViewModel(viewModel: LocationDetailsViewModel): ViewModel
}