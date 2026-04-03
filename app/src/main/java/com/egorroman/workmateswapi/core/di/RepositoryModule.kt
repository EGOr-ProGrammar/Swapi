package com.egorroman.workmateswapi.core.di

import com.egorroman.workmateswapi.core.data.repository.CharactersRepositoryImpl
import com.egorroman.workmateswapi.core.domain.repository.CharactersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharactersRepository(
        impl: CharactersRepositoryImpl
    ): CharactersRepository
}