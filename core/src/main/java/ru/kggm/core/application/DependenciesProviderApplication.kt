package ru.kggm.core.application

import ru.kggm.core.di.DependenciesProvider

interface DependenciesProviderApplication {
    fun getDependenciesProvider(): DependenciesProvider
}