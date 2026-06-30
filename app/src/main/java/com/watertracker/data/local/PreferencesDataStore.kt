package com.watertracker.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.watertracker.domain.model.AppTheme
import com.watertracker.domain.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "watertracker_prefs")

@Singleton
class PreferencesDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val APP_THEME = stringPreferencesKey("app_theme")
        val ADS = booleanPreferencesKey("ads")
        val CONSENT = booleanPreferencesKey("consent")
        val ANALYTICS = booleanPreferencesKey("analytics")
        val PERSONALIZED_ADS = booleanPreferencesKey("personalized_ads")
        val LANGUAGE = stringPreferencesKey("language")
        val GOAL = intPreferencesKey("daily_goal_ml")
        val REMINDERS = booleanPreferencesKey("reminders")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            appTheme = runCatching { AppTheme.valueOf(prefs[Keys.APP_THEME] ?: AppTheme.SYSTEM.name) }.getOrDefault(AppTheme.SYSTEM),
            adsEnabled = prefs[Keys.ADS] ?: true,
            consentGiven = prefs[Keys.CONSENT] ?: true,
            analyticsEnabled = prefs[Keys.ANALYTICS] ?: true,
            personalizedAds = prefs[Keys.PERSONALIZED_ADS] ?: false,
            language = prefs[Keys.LANGUAGE] ?: "system",
            dailyGoalMl = prefs[Keys.GOAL] ?: 2000,
            remindersEnabled = prefs[Keys.REMINDERS] ?: false
        )
    }

    suspend fun update(transform: (UserPreferences) -> UserPreferences) {
        context.dataStore.edit { prefs ->
            val current = UserPreferences(
                appTheme = runCatching { AppTheme.valueOf(prefs[Keys.APP_THEME] ?: AppTheme.SYSTEM.name) }.getOrDefault(AppTheme.SYSTEM),
                adsEnabled = prefs[Keys.ADS] ?: true,
                consentGiven = prefs[Keys.CONSENT] ?: true,
                analyticsEnabled = prefs[Keys.ANALYTICS] ?: true,
                personalizedAds = prefs[Keys.PERSONALIZED_ADS] ?: false,
                language = prefs[Keys.LANGUAGE] ?: "system",
                dailyGoalMl = prefs[Keys.GOAL] ?: 2000,
                remindersEnabled = prefs[Keys.REMINDERS] ?: false
            )
            val updated = transform(current)
            prefs[Keys.APP_THEME] = updated.appTheme.name
            prefs[Keys.ADS] = updated.adsEnabled
            prefs[Keys.CONSENT] = updated.consentGiven
            prefs[Keys.ANALYTICS] = updated.analyticsEnabled
            prefs[Keys.PERSONALIZED_ADS] = updated.personalizedAds
            prefs[Keys.LANGUAGE] = updated.language
            prefs[Keys.GOAL] = updated.dailyGoalMl
            prefs[Keys.REMINDERS] = updated.remindersEnabled
        }
    }
}
