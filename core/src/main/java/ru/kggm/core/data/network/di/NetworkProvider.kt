package ru.kggm.core.data.network.di

import retrofit2.Retrofit

interface NetworkProvider {
    fun provideRetrofit(): Retrofit
}