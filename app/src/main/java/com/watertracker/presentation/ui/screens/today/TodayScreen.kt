package com.watertracker.presentation.ui.screens.today

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.watertracker.ads.AdManager
import com.watertracker.engine.WaterTrackerEngine
import com.watertracker.presentation.ui.components.AdBanner
import com.watertracker.presentation.viewmodel.WaterTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(adManager: AdManager, adsEnabled: Boolean, viewModel: WaterTrackerViewModel = hiltViewModel()) {
    val total by viewModel.todayTotal.collectAsStateWithLifecycle()
    val prefs by viewModel.prefs.collectAsStateWithLifecycle()
    val engine = WaterTrackerEngine()
    val progress = engine.progressPercent(total, prefs.dailyGoalMl)

    Scaffold(topBar = { TopAppBar(title = { Text("Today") }) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AdBanner(adManager = adManager, adsEnabled = adsEnabled)
            Canvas(Modifier.size(200.dp)) {
                drawArc(color = androidx.compose.ui.graphics.Color.LightGray, startAngle = -90f, sweepAngle = 360f, useCenter = false, style = Stroke(20f, cap = StrokeCap.Round))
                drawArc(color = androidx.compose.ui.graphics.Color(0xFF2196F3), startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, style = Stroke(20f, cap = StrokeCap.Round))
            }
            Text("$total / ${prefs.dailyGoalMl} ml", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("${(progress * 100).toInt()}% of daily goal", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
