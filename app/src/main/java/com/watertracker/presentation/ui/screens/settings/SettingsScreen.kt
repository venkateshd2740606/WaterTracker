package com.watertracker.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.watertracker.presentation.viewmodel.WaterTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: WaterTrackerViewModel = hiltViewModel()) {
    val prefs by viewModel.prefs.collectAsStateWithLifecycle()
    var goalText by remember(prefs.dailyGoalMl) { mutableStateOf(prefs.dailyGoalMl.toString()) }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = goalText, onValueChange = { goalText = it },
                label = { Text("Daily goal (ml)") }, modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.material3.Button(
                onClick = { goalText.toIntOrNull()?.let { viewModel.setGoal(it) } },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) { Text("Save Goal") }
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Reminders every 2 hours", Modifier.weight(1f))
                Switch(checked = prefs.remindersEnabled, onCheckedChange = { viewModel.setReminders(it) })
            }
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Ads", Modifier.weight(1f))
                Switch(checked = prefs.adsEnabled, onCheckedChange = { viewModel.setAds(it) })
            }
        }
    }
}
