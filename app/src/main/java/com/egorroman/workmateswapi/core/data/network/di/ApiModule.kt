package com.egorroman.workmateswapi.core.data.network.di

import com.egorroman.workmateswapi.core.data.network.api.SwapiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideSwapiApi(retrofit: Retrofit): SwapiApi {
        return retrofit.create(SwapiApi::class.java)
    }
}