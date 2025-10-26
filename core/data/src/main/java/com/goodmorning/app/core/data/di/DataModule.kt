package com.goodmorning.app.core.data.di

import com.goodmorning.app.core.data.GreetingRepositoryImpl
import com.goodmorning.app.core.domain.GreetingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindGreetingRepository(impl: GreetingRepositoryImpl): GreetingRepository
}
