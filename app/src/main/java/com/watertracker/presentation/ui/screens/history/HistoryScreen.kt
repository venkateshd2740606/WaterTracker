package com.watertracker.presentation.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.watertracker.presentation.viewmodel.WaterTrackerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: WaterTrackerViewModel = hiltViewModel()) {
    val weekly by viewModel.weeklyTotals.collectAsStateWithLifecycle()
    val logs by viewModel.todayLogs.collectAsStateWithLifecycle()
    val max = (weekly.maxOfOrNull { it.totalMl } ?: 1).coerceAtLeast(1)
    val fmt = SimpleDateFormat("HH:mm", Locale.getDefault())

    Scaffold(topBar = { TopAppBar(title = { Text("History") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Weekly intake", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.fillMaxWidth().height(160.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                weekly.forEach { day ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier.height((day.totalMl.toFloat() / max * 120).dp.coerceAtLeast(4.dp)).fillMaxWidth(0.1f)
                                .padding(horizontal = 2.dp)
                        ) {
                            androidx.compose.foundation.layout.Box(
                                Modifier.fillMaxSize().align(Alignment.BottomCenter)
                                    .then(Modifier.fillMaxWidth())
                            ) {
                                androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
                                    drawRect(color = androidx.compose.ui.graphics.Color(0xFF42A5F5), size = size)
                                }
                            }
                        }
                        Text(day.date.takeLast(5), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Text("Today's logs", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(logs, key = { it.id }) { log ->
                    Card(Modifier.fillMaxWidth()) {
                        Text("${log.amountMl} ml at ${fmt.format(Date(log.timestamp))}", Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}
