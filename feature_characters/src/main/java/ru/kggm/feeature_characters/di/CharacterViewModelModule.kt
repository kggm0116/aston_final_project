package ru.kggm.feeature_characters.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kggm.core.presentation.di.ViewModelKey
import ru.kggm.feeature_characters.presentation.ui.CharactersViewModel

@Module
interface CharacterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharactersViewModel::class)
    fun bindProfileViewModel(viewModel: CharactersViewModel): ViewModel
}