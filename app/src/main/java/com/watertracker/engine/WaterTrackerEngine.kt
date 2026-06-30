package com.watertracker.engine

import com.watertracker.domain.model.DayTotal
import com.watertracker.domain.model.WaterLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterTrackerEngine @Inject constructor() {
    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE

    fun todayDate(): String = LocalDate.now().format(dateFmt)

    fun weekDates(end: LocalDate = LocalDate.now()): List<String> =
        (6 downTo 0).map { end.minusDays(it.toLong()).format(dateFmt) }

    fun weeklyTotals(logs: List<WaterLog>, dates: List<String>): List<DayTotal> {
        val grouped = logs.groupBy { it.date }.mapValues { (_, v) -> v.sumOf { it.amountMl } }
        return dates.map { DayTotal(it, grouped[it] ?: 0) }
    }

    fun progressPercent(totalMl: Int, goalMl: Int): Float =
        if (goalMl <= 0) 0f else (totalMl.toFloat() / goalMl).coerceIn(0f, 1f)
}
