package ru.kggm.feature_browse.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kggm.core.presentation.di.ViewModelKey
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListViewModel
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListViewModel
import ru.kggm.feature_browse.presentation.ui.locations.list.LocationListViewModel

@Module
interface LocationListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationListViewModel::class)
    fun bindCharactersViewModel(viewModel: LocationListViewModel): ViewModel
}