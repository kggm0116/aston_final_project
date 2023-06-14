package ru.kggm.feature_browse.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kggm.core.presentation.di.ViewModelKey
import ru.kggm.feature_browse.presentation.ui.characters.CharactersViewModel

@Module
interface CharactersViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    fun bindCharactersViewModel(viewModel: CharactersViewModel): ViewModel
}