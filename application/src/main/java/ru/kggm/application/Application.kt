package ru.kggm.application

import ru.kggm.application.di.ApplicationComponent
import ru.kggm.core.application.DependenciesProviderApplication

class Application: android.app.Application(), DependenciesProviderApplication  {
    private val component by lazy { ApplicationComponent.init(applicationContext) }
    override fun getDependenciesProvider() = component
}