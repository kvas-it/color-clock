package com.colorclock.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.colorclock.app.data.isNightTime
import com.colorclock.app.ui.AppNavigation
import com.colorclock.app.ui.theme.ColorClockTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Immersive mode: hide system bars
        enableEdgeToEdge()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            val vm: ClockViewModel = viewModel()
            val clockState by vm.clockState.collectAsState()
            val settings by vm.settings.collectAsState()

            // Adjust window brightness based on current mode
            val brightness = if (clockState.isNight) {
                settings.nightBrightness
            } else {
                settings.dayBrightness
            }
            window.attributes = window.attributes.also {
                it.screenBrightness = brightness
            }

            ColorClockTheme {
                AppNavigation(vm)
            }
        }
    }
}
