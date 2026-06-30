package com.watertracker.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class WaterTrackerEngineTest {
    private val engine = WaterTrackerEngine()

    @Test
    fun progressPercent_capsAtOne() {
        assertEquals(1f, engine.progressPercent(3000, 2000))
    }

    @Test
    fun weekDates_hasSevenDays() {
        assertEquals(7, engine.weekDates().size)
    }
}
