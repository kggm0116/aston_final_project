package ru.kggm.feature_browse.data.database.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [SharedDatabaseModule::class]
)
interface SharedDatabaseComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): SharedDatabaseComponent
    }

    companion object {
        fun init(context: Context): SharedDatabaseComponent {
            return DaggerSharedDatabaseComponent.factory()
                .create(context)
        }
    }
}