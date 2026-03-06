package com.colorclock.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colorclock.app.ClockState
import com.colorclock.app.R
import com.colorclock.app.data.ClockSettings
import com.colorclock.app.ui.theme.toComposeColor
import kotlinx.coroutines.delay

val NunitoFont = FontFamily(
    Font(R.font.nunito_bold, FontWeight.Bold),
)

@Composable
fun ClockScreen(
    clockState: ClockState,
    settings: ClockSettings,
    onOpenSettings: () -> Unit,
) {
    val bgColor = if (clockState.isNight) {
        settings.nightBackgroundColor.toComposeColor()
    } else {
        settings.dayBackgroundColor.toComposeColor()
    }
    val digitColor = if (clockState.isNight) {
        settings.nightDigitColor.toComposeColor()
    } else {
        settings.dayDigitColor.toComposeColor()
    }

    // Settings icon visibility: show on tap, auto-hide after 3 seconds
    var showSettingsIcon by remember { mutableStateOf(true) }

    LaunchedEffect(showSettingsIcon) {
        if (showSettingsIcon) {
            delay(4000)
            showSettingsIcon = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { showSettingsIcon = true },
        contentAlignment = Alignment.Center,
    ) {
        // Main time display
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = clockState.displayTime,
                color = digitColor,
                fontSize = 180.sp,
                fontFamily = NunitoFont,
                fontWeight = FontWeight.Bold,
            )
            if (clockState.amPm.isNotEmpty()) {
                Text(
                    text = clockState.amPm,
                    color = digitColor.copy(alpha = 0.7f),
                    fontSize = 48.sp,
                    fontFamily = NunitoFont,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 28.dp, start = 12.dp),
                )
            }
        }

        // Settings gear icon (top-right, auto-hides)
        if (showSettingsIcon) {
            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = digitColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}
