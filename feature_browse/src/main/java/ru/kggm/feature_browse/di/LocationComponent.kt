package ru.kggm.feature_browse.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.di.ViewModelFactoryModule
import ru.kggm.feature_browse.data.database.di.LocationDatabaseModule
import ru.kggm.feature_browse.data.database.di.SharedDatabaseModule
import ru.kggm.feature_browse.data.network.di.LocationNetworkModule
import ru.kggm.feature_browse.presentation.di.CharacterDetailsViewModelModule
import ru.kggm.feature_browse.presentation.di.CharacterListViewModelModule
import ru.kggm.feature_browse.presentation.di.LocationDetailsViewModelModule
import ru.kggm.feature_browse.presentation.di.LocationListViewModelModule
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.filter.CharacterFilterFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import ru.kggm.feature_browse.presentation.ui.locations.details.LocationDetailsFragment
import ru.kggm.feature_browse.presentation.ui.locations.list.LocationListFragment
import ru.kggm.feature_browse.presentation.ui.locations.list.filter.LocationFilterFragment
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DependenciesProvider::class],
    modules = [
        LocationNetworkModule::class,
        SharedDatabaseModule::class,
        LocationDatabaseModule::class,

        LocationRepositoryModule::class,

        ViewModelFactoryModule::class,

        LocationListViewModelModule::class,
        LocationDetailsViewModelModule::class,
    ],
)

interface LocationComponent {
    fun inject(fragment: LocationListFragment)
    fun inject(fragment: LocationDetailsFragment)
    fun inject(fragment: LocationFilterFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependenciesProvider: DependenciesProvider
        ): LocationComponent
    }

    companion object {
        fun init(
            context: Context,
            dependenciesProvider: DependenciesProvider,
        ): LocationComponent {
            return DaggerLocationComponent.factory()
                .create(
                    context = context,
                    dependenciesProvider = dependenciesProvider
                )
        }
    }
}