package com.watertracker.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.watertracker.ads.AdManager
import com.watertracker.analytics.AnalyticsManager
import com.watertracker.domain.model.UserPreferences
import com.watertracker.domain.repository.PreferencesRepository
import com.watertracker.presentation.navigation.Screen
import com.watertracker.presentation.navigation.WaterTrackerNavHost
import com.watertracker.presentation.ui.theme.WaterTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var adManager: AdManager
    @Inject lateinit var analyticsManager: AnalyticsManager
    @Inject lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        adManager.initialize()
        setContent {
            val prefs by preferencesRepository.getUserPreferences().collectAsStateWithLifecycle(initialValue = null)
            if (prefs == null) { WaterTrackerTheme { Box(Modifier.fillMaxSize()) }; return@setContent }
            Root(prefs!!, adManager, analyticsManager)
        }
    }
}

@Composable
private fun Root(prefs: UserPreferences, adManager: AdManager, analyticsManager: AnalyticsManager) {
    LaunchedEffect(prefs.analyticsEnabled) { analyticsManager.setCollectionEnabled(prefs.analyticsEnabled) }
    LaunchedEffect(prefs.adsEnabled, prefs.personalizedAds) { adManager.updateAdPolicy(prefs.adsEnabled, prefs.personalizedAds) }
    WaterTrackerTheme(prefs.appTheme) {
        WaterTrackerNavHost(rememberNavController(), adManager, analyticsManager, prefs, Screen.Today.route)
    }
}
