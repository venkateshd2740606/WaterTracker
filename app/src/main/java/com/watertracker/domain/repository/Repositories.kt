package com.watertracker.domain.repository

import com.watertracker.domain.model.UserPreferences
import com.watertracker.domain.model.WaterLog
import kotlinx.coroutines.flow.Flow

interface WaterLogRepository {
    fun observeLogsForDate(date: String): Flow<List<WaterLog>>
    fun observeTotalForDate(date: String): Flow<Int>
    fun observeBetween(start: String, end: String): Flow<List<WaterLog>>
    suspend fun logWater(log: WaterLog): Long
    suspend fun deleteLog(id: Long)
}

interface PreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun updatePreferences(transform: (UserPreferences) -> UserPreferences)
}
