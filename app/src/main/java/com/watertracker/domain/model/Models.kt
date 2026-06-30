package com.watertracker.domain.model

enum class AppTheme(val displayName: String) {
    SYSTEM("System"), LIGHT("Light"), DARK("Dark")
}

data class UserPreferences(
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val adsEnabled: Boolean = true,
    val consentGiven: Boolean = true,
    val analyticsEnabled: Boolean = true,
    val personalizedAds: Boolean = false,
    val language: String = "system",
    val dailyGoalMl: Int = 2000,
    val remindersEnabled: Boolean = false
)

data class WaterLog(
    val id: Long = 0,
    val date: String,
    val amountMl: Int,
    val timestamp: Long
)

data class DayTotal(val date: String, val totalMl: Int)
