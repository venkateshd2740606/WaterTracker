package com.watertracker.di

import com.watertracker.data.repository.PreferencesRepositoryImpl
import com.watertracker.data.repository.WaterLogRepositoryImpl
import com.watertracker.domain.repository.PreferencesRepository
import com.watertracker.domain.repository.WaterLogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindWaterLogRepository(impl: WaterLogRepositoryImpl): WaterLogRepository
    @Binds @Singleton abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
