package com.watertracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.watertracker.data.local.database.dao.WaterLogDao
import com.watertracker.data.local.database.entity.WaterLogEntity

@Database(entities = [WaterLogEntity::class], version = 1, exportSchema = true)
abstract class WaterTrackerDatabase : RoomDatabase() {
    abstract fun waterLogDao(): WaterLogDao
}
