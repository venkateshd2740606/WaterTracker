package com.watertracker.di

import android.content.Context
import androidx.room.Room
import com.watertracker.data.local.database.WaterTrackerDatabase
import com.watertracker.data.local.database.dao.WaterLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WaterTrackerDatabase =
        Room.databaseBuilder(context, WaterTrackerDatabase::class.java, "watertracker.db")
            .fallbackToDestructiveMigration().build()

    @Provides fun provideWaterLogDao(db: WaterTrackerDatabase): WaterLogDao = db.waterLogDao()
}
