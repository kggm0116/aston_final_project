package ru.kggm.feeature_characters.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.di.ViewModelFactoryModule
import ru.kggm.feeature_characters.data.database.di.CharacterDatabaseModule
import ru.kggm.feeature_characters.data.network.di.CharacterNetworkModule
import ru.kggm.feeature_characters.presentation.ui.CharactersFragment
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DependenciesProvider::class],
    modules = [
        CharacterNetworkModule::class,
        CharacterRepositoryModule::class,
        ViewModelFactoryModule::class,
        CharacterViewModelModule::class,
        CharacterDatabaseModule::class
    ],
)
interface CharacterComponent {
    fun inject(fragment: CharactersFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependenciesProvider: DependenciesProvider
        ): CharacterComponent
    }

    companion object {
        fun init(
            context: Context,
            dependenciesProvider: DependenciesProvider,
        ): CharacterComponent {
            return DaggerCharacterComponent.factory()
                .create(
                    context = context,
                    dependenciesProvider = dependenciesProvider
                )
        }
    }
}