package com.colorclock.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.colorclock.app.data.ClockSettings
import com.colorclock.app.ui.theme.presetBackgroundColors
import com.colorclock.app.ui.theme.presetDigitColors
import com.colorclock.app.ui.theme.toComposeColor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: ClockSettings,
    onSettingsChange: (ClockSettings) -> Unit,
    onBack: () -> Unit,
) {
    var current by remember(settings) { mutableStateOf(settings) }

    // Save on every change
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
            // Preview
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
            ColorPickerRow(
                label = "Background",
                selectedColor = current.dayBackgroundColor,
                colors = presetBackgroundColors,
                onColorSelected = {
                    update(current.copy(dayBackgroundColor = it))
                },
            )
            ColorPickerRow(
                label = "Digits",
                selectedColor = current.dayDigitColor,
                colors = presetDigitColors,
                onColorSelected = {
                    update(current.copy(dayDigitColor = it))
                },
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
            ColorPickerRow(
                label = "Background",
                selectedColor = current.nightBackgroundColor,
                colors = presetBackgroundColors,
                onColorSelected = {
                    update(current.copy(nightBackgroundColor = it))
                },
            )
            ColorPickerRow(
                label = "Digits",
                selectedColor = current.nightDigitColor,
                colors = presetDigitColors,
                onColorSelected = {
                    update(current.copy(nightDigitColor = it))
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
        // Day preview
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
        // Night preview
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
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onTimeChange(state.hour, state.minute)
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = state) },
        )
    }
}

@Composable
private fun ColorPickerRow(
    label: String,
    selectedColor: Long,
    colors: List<Pair<Long, String>>,
    onColorSelected: (Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(label, modifier = Modifier.width(80.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(selectedColor.toComposeColor())
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clickable { showDialog = true },
        )
        val colorName = colors.find { it.first == selectedColor }?.second ?: ""
        if (colorName.isNotEmpty()) {
            Text(colorName, style = MaterialTheme.typography.bodySmall)
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Choose $label Color",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(200.dp),
                    ) {
                        items(colors) { (colorVal, name) ->
                            val isSelected = colorVal == selectedColor
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(colorVal.toComposeColor())
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
                                                Color.Gray.copy(alpha = 0.3f),
                                                CircleShape,
                                            )
                                        }
                                    )
                                    .clickable {
                                        onColorSelected(colorVal)
                                        showDialog = false
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = "Selected",
                                        tint = if (colorVal == 0xFF000000L
                                            || colorVal == 0xFF1A237E
                                            || colorVal == 0xFF4A148C
                                            || colorVal == 0xFF212121
                                            || colorVal == 0xFF1B5E20
                                            || colorVal == 0xFF311B92
                                            || colorVal == 0xFF0D47A1
                                            || colorVal == 0xFF3E2723
                                        ) Color.White else Color.Black,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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
