package com.watertracker.presentation.ui.screens.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.watertracker.presentation.viewmodel.WaterTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(viewModel: WaterTrackerViewModel = hiltViewModel()) {
    var custom by remember { mutableStateOf("250") }

    Scaffold(topBar = { TopAppBar(title = { Text("Log Water") }) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { viewModel.logWater(250) }, modifier = Modifier.fillMaxWidth()) { Text("Glass (+250 ml)") }
            Button(onClick = { viewModel.logWater(200) }, modifier = Modifier.fillMaxWidth()) { Text("Cup (+200 ml)") }
            OutlinedTextField(value = custom, onValueChange = { custom = it }, label = { Text("Custom amount (ml)") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = { custom.toIntOrNull()?.let { viewModel.logWater(it) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = custom.toIntOrNull() != null
            ) { Text("Add Custom") }
        }
    }
}
