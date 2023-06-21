package ru.kggm.feature_browse.presentation.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kggm.core.presentation.di.ViewModelKey
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListViewModel

@Module
interface CharacterListViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharacterListViewModel::class)
    fun bindCharactersViewModel(viewModel: CharacterListViewModel): ViewModel
}