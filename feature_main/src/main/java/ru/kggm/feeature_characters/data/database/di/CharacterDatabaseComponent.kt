package ru.kggm.feeature_characters.data.database.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [CharacterDatabaseModule::class]
)
interface CharacterDatabaseComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): CharacterDatabaseComponent
    }

    companion object {
        fun init(context: Context): CharacterDatabaseComponent {
            return DaggerCharacterDatabaseComponent.factory()
                .create(context)
        }
    }
}