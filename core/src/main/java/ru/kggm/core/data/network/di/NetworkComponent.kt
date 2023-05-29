package ru.kggm.core.data.network.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkProvider {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): NetworkComponent
    }

    companion object {
        fun init(context: Context): NetworkComponent {
            return DaggerNetworkComponent.factory()
                .create(context)
        }
    }
}