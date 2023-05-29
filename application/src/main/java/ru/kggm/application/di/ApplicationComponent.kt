package ru.kggm.application.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kggm.core.di.DependenciesProvider
import ru.kggm.core.data.network.di.NetworkComponent
import ru.kggm.core.data.network.di.NetworkProvider
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class]
)
interface ApplicationComponent : DependenciesProvider {
    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            @BindsInstance context: Context
        ): ApplicationComponent
    }

    companion object {
        fun init(context: Context): ApplicationComponent {
            return DaggerApplicationComponent.factory()
                .create(
                    networkProvider = NetworkComponent.init(context),
                    context = context
                )
        }
    }
}