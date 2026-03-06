package com.colorclock.app.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.colorclock.app.data.ClockSettings
import com.colorclock.app.ui.theme.toComposeColor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    settings: ClockSettings,
    onSettingsChange: (ClockSettings) -> Unit,
    onBack: () -> Unit,
) {
    var current by remember(settings) { mutableStateOf(settings) }

    fun update(new: ClockSettings) {
        current = new
        onSettingsChange(new)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PreviewStrip(current)

            // Time format
            SectionHeader("Time Format")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("24-hour format")
                Switch(
                    checked = current.use24HourFormat,
                    onCheckedChange = { update(current.copy(use24HourFormat = it)) },
                )
            }

            // Day settings
            SectionHeader("Day Mode")
            TimePickerRow(
                label = "Starts at",
                hour = current.dayStartHour,
                minute = current.dayStartMinute,
                onTimeChange = { h, m ->
                    update(current.copy(dayStartHour = h, dayStartMinute = m))
                },
            )
            ColorPresetRow(
                label = "Background",
                selectedColor = current.dayBackgroundColor,
                presets = current.dayBgPresets,
                onColorSelected = { update(current.copy(dayBackgroundColor = it)) },
                onPresetsChanged = { update(current.copy(dayBgPresets = it)) },
            )
            ColorPresetRow(
                label = "Digits",
                selectedColor = current.dayDigitColor,
                presets = current.dayDigitPresets,
                onColorSelected = { update(current.copy(dayDigitColor = it)) },
                onPresetsChanged = { update(current.copy(dayDigitPresets = it)) },
            )
            BrightnessSlider(
                value = current.dayBrightness,
                onValueChange = { update(current.copy(dayBrightness = it)) },
            )

            // Night settings
            SectionHeader("Night Mode")
            TimePickerRow(
                label = "Starts at",
                hour = current.nightStartHour,
                minute = current.nightStartMinute,
                onTimeChange = { h, m ->
                    update(current.copy(nightStartHour = h, nightStartMinute = m))
                },
            )
            ColorPresetRow(
                label = "Background",
                selectedColor = current.nightBackgroundColor,
                presets = current.nightBgPresets,
                onColorSelected = {
                    update(current.copy(nightBackgroundColor = it))
                },
                onPresetsChanged = {
                    update(current.copy(nightBgPresets = it))
                },
            )
            ColorPresetRow(
                label = "Digits",
                selectedColor = current.nightDigitColor,
                presets = current.nightDigitPresets,
                onColorSelected = {
                    update(current.copy(nightDigitColor = it))
                },
                onPresetsChanged = {
                    update(current.copy(nightDigitPresets = it))
                },
            )
            BrightnessSlider(
                value = current.nightBrightness,
                onValueChange = { update(current.copy(nightBrightness = it)) },
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PreviewStrip(settings: ClockSettings) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(settings.dayBackgroundColor.toComposeColor()),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "7:00 AM",
                color = settings.dayDigitColor.toComposeColor(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = NunitoFont,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(settings.nightBackgroundColor.toComposeColor()),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "9:30 PM",
                color = settings.nightDigitColor.toComposeColor(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = NunitoFont,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerRow(
    label: String,
    hour: Int,
    minute: Int,
    onTimeChange: (Int, Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val timeText = "%02d:%02d".format(hour, minute)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(label, modifier = Modifier.width(80.dp))
        OutlinedButton(onClick = { showDialog = true }) {
            Text(timeText, fontSize = 18.sp)
        }
    }

    if (showDialog) {
        val state = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = true,
        )
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TimeInput(state = state)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            onTimeChange(state.hour, state.minute)
                            showDialog = false
                        }) { Text("OK") }
                    }
                }
            }
        }
    }
}

// Tap a swatch to select it. Long-press to edit it with the HSV picker.
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColorPresetRow(
    label: String,
    selectedColor: Long,
    presets: List<Long>,
    onColorSelected: (Long) -> Unit,
    onPresetsChanged: (List<Long>) -> Unit,
) {
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(label, modifier = Modifier.width(80.dp))
        presets.forEachIndexed { index, color ->
            val isSelected = color == selectedColor
            val composeColor = color.toComposeColor()
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(composeColor)
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                3.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape,
                            )
                        } else {
                            Modifier.border(
                                1.dp,
                                Color.Gray.copy(alpha = 0.4f),
                                CircleShape,
                            )
                        }
                    )
                    .combinedClickable(
                        onClick = { onColorSelected(color) },
                        onLongClick = { editingIndex = index },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (isSelected) {
                    val checkColor = if (composeColor.luminance() > 0.5f) {
                        Color.Black
                    } else {
                        Color.White
                    }
                    Icon(
                        Icons.Filled.Check, "Selected",
                        tint = checkColor,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }

    editingIndex?.let { idx ->
        HsvColorPickerDialog(
            initialColor = presets[idx],
            onColorPicked = { newColor ->
                val newPresets = presets.toMutableList()
                newPresets[idx] = newColor
                onPresetsChanged(newPresets)
                onColorSelected(newColor)
                editingIndex = null
            },
            onDismiss = { editingIndex = null },
        )
    }
}

@Composable
private fun BrightnessSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Brightness", modifier = Modifier.width(80.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.01f..1f,
            modifier = Modifier.weight(1f),
        )
        Text(
            "${(value * 100).roundToInt()}%",
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
