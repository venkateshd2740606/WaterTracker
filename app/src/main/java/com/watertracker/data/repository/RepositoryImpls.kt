package com.watertracker.data.repository

import com.watertracker.data.local.PreferencesDataStore
import com.watertracker.data.local.database.dao.WaterLogDao
import com.watertracker.data.local.database.entity.WaterLogEntity
import com.watertracker.domain.model.UserPreferences
import com.watertracker.domain.model.WaterLog
import com.watertracker.domain.repository.PreferencesRepository
import com.watertracker.domain.repository.WaterLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterLogRepositoryImpl @Inject constructor(private val dao: WaterLogDao) : WaterLogRepository {
    override fun observeLogsForDate(date: String): Flow<List<WaterLog>> =
        dao.observeByDate(date).map { list -> list.map { it.toDomain() } }

    override fun observeTotalForDate(date: String): Flow<Int> = dao.observeTotalForDate(date)

    override fun observeBetween(start: String, end: String): Flow<List<WaterLog>> =
        dao.observeBetween(start, end).map { list -> list.map { it.toDomain() } }

    override suspend fun logWater(log: WaterLog): Long = dao.insert(log.toEntity())
    override suspend fun deleteLog(id: Long) = dao.delete(id)
}

@Singleton
class PreferencesRepositoryImpl @Inject constructor(private val dataStore: PreferencesDataStore) : PreferencesRepository {
    override fun getUserPreferences(): Flow<UserPreferences> = dataStore.preferencesFlow
    override suspend fun updatePreferences(transform: (UserPreferences) -> UserPreferences) = dataStore.update(transform)
}

private fun WaterLogEntity.toDomain() = WaterLog(id, date, amountMl, timestamp)
private fun WaterLog.toEntity() = WaterLogEntity(id, date, amountMl, timestamp)
