package ru.kggm.feature_browse.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.di.ViewModelFactoryModule
import ru.kggm.feature_browse.data.database.di.CharacterDatabaseModule
import ru.kggm.feature_browse.data.database.di.LocationDatabaseModule
import ru.kggm.feature_browse.data.database.di.SharedDatabaseModule
import ru.kggm.feature_browse.data.network.di.CharacterNetworkModule
import ru.kggm.feature_browse.data.network.di.LocationNetworkModule
import ru.kggm.feature_browse.presentation.di.CharacterDetailsViewModelModule
import ru.kggm.feature_browse.presentation.di.CharacterListViewModelModule
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.filter.CharacterFilterFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DependenciesProvider::class],
    modules = [
        CharacterNetworkModule::class,
        LocationNetworkModule::class,

        SharedDatabaseModule::class,
        CharacterDatabaseModule::class,
        LocationDatabaseModule::class,

        CharacterRepositoryModule::class,
        LocationRepositoryModule::class,

        ViewModelFactoryModule::class,

        CharacterListViewModelModule::class,
        CharacterDetailsViewModelModule::class,
    ],
)

interface CharacterComponent {
    fun inject(fragment: CharacterListFragment)
    fun inject(fragment: CharacterDetailsFragment)
    fun inject(fragment: CharacterFilterFragment)

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