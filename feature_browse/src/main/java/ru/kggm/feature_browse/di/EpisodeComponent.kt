package ru.kggm.feature_browse.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.presentation.di.ViewModelFactoryModule
import ru.kggm.feature_browse.data.database.di.EpisodeDatabaseModule
import ru.kggm.feature_browse.data.database.di.SharedDatabaseModule
import ru.kggm.feature_browse.data.network.di.EpisodeNetworkModule
import ru.kggm.feature_browse.presentation.di.CharacterDetailsViewModelModule
import ru.kggm.feature_browse.presentation.di.CharacterListViewModelModule
import ru.kggm.feature_browse.presentation.di.EpisodeDetailsViewModelModule
import ru.kggm.feature_browse.presentation.di.EpisodeListViewModelModule
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsFragment
import ru.kggm.feature_browse.presentation.ui.characters.filter.CharacterFilterFragment
import ru.kggm.feature_browse.presentation.ui.characters.list.CharacterListFragment
import ru.kggm.feature_browse.presentation.ui.episodes.details.EpisodeDetailsFragment
import ru.kggm.feature_browse.presentation.ui.episodes.list.EpisodeListFragment
import ru.kggm.feature_browse.presentation.ui.episodes.list.filter.EpisodeFilterFragment
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DependenciesProvider::class],
    modules = [
        EpisodeNetworkModule::class,
        SharedDatabaseModule::class,
        EpisodeDatabaseModule::class,

        EpisodeRepositoryModule::class,

        ViewModelFactoryModule::class,

        EpisodeListViewModelModule::class,
        EpisodeDetailsViewModelModule::class,
    ],
)

interface EpisodeComponent {
    fun inject(fragment: EpisodeListFragment)
    fun inject(fragment: EpisodeDetailsFragment)
    fun inject(fragment: EpisodeFilterFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            dependenciesProvider: DependenciesProvider
        ): EpisodeComponent
    }

    companion object {
        fun init(
            context: Context,
            dependenciesProvider: DependenciesProvider,
        ): EpisodeComponent {
            return DaggerEpisodeComponent.factory()
                .create(
                    context = context,
                    dependenciesProvider = dependenciesProvider
                )
        }
    }
}