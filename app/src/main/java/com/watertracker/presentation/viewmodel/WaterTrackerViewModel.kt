package com.watertracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.watertracker.domain.model.UserPreferences
import com.watertracker.domain.model.WaterLog
import com.watertracker.domain.repository.PreferencesRepository
import com.watertracker.domain.repository.WaterLogRepository
import com.watertracker.engine.WaterTrackerEngine
import com.watertracker.worker.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterTrackerViewModel @Inject constructor(
    private val waterLogRepository: WaterLogRepository,
    private val preferencesRepository: PreferencesRepository,
    private val engine: WaterTrackerEngine,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val today = engine.todayDate()

    val prefs: StateFlow<UserPreferences> = preferencesRepository.getUserPreferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    val todayTotal: StateFlow<Int> = waterLogRepository.observeTotalForDate(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayLogs = waterLogRepository.observeLogsForDate(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weekDates = engine.weekDates()
    val weeklyLogs = waterLogRepository.observeBetween(weekDates.first(), weekDates.last())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weeklyTotals = combine(weeklyLogs, prefs) { logs, p ->
        engine.weeklyTotals(logs, weekDates)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            waterLogRepository.logWater(WaterLog(date = today, amountMl = amountMl, timestamp = System.currentTimeMillis()))
        }
    }

    fun setGoal(ml: Int) {
        viewModelScope.launch { preferencesRepository.updatePreferences { it.copy(dailyGoalMl = ml.coerceIn(500, 5000)) } }
    }

    fun setReminders(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updatePreferences { it.copy(remindersEnabled = enabled) }
            if (enabled) ReminderWorker.schedule(context) else ReminderWorker.cancel(context)
        }
    }

    fun setAds(enabled: Boolean) = viewModelScope.launch { preferencesRepository.updatePreferences { it.copy(adsEnabled = enabled) } }
}
