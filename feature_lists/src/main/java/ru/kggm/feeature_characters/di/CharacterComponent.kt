package ru.kggm.feeature_characters.di

import dagger.Component
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.di.ViewModelFactoryModule
import ru.kggm.feeature_characters.presentation.ui.CharactersFragment
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DependenciesProvider::class],
    modules = [
        CharacterNetworkModule::class,
        CharacterRepositoryModule::class,
        ViewModelFactoryModule::class,
        CharacterViewModelModule::class
    ],
)
interface CharacterComponent {
    fun inject(fragment: CharactersFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependenciesProvider: DependenciesProvider
        ): CharacterComponent
    }

    companion object {
        fun init(
            dependenciesProvider: DependenciesProvider,
        ): CharacterComponent {
            return DaggerCharacterComponent.factory()
                .create(
                    dependenciesProvider = dependenciesProvider
                )
        }
    }
}