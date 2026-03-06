package com.colorclock.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

fun hsvToColor(hue: Float, sat: Float, value: Float): Color {
    val argb = android.graphics.Color.HSVToColor(floatArrayOf(hue, sat, value))
    return Color(argb)
}

fun Color.toHsv(): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt(),
        hsv,
    )
    return hsv
}

fun Long.toHsv(): FloatArray = Color(this).toHsv()

@Composable
fun HsvColorPickerDialog(
    initialColor: Long,
    onColorPicked: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val initHsv = remember(initialColor) { initialColor.toHsv() }
    var hue by remember { mutableFloatStateOf(initHsv[0]) }
    var sat by remember { mutableFloatStateOf(initHsv[1]) }
    var value by remember { mutableFloatStateOf(initHsv[2]) }

    val currentColor = hsvToColor(hue, sat, value)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(IntrinsicSize.Min),
                ) {
                    // Saturation-Value square
                    SaturationValuePanel(
                        hue = hue,
                        sat = sat,
                        value = value,
                        onSatValueChange = { s, v -> sat = s; value = v },
                        modifier = Modifier.size(180.dp),
                    )
                    // Vertical hue bar
                    HueBar(
                        hue = hue,
                        onHueChange = { hue = it },
                        modifier = Modifier
                            .width(32.dp)
                            .fillMaxHeight(),
                    )
                    // Preview swatch
                    Canvas(
                        modifier = Modifier
                            .width(40.dp)
                            .fillMaxHeight(),
                    ) {
                        drawRect(currentColor)
                    }
                }
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = {
                        val argb = android.graphics.Color.HSVToColor(
                            floatArrayOf(hue, sat, value)
                        )
                        // Convert to unsigned Long with full alpha
                        val colorLong = argb.toLong() and 0xFFFFFFFFL
                        onColorPicked(colorLong)
                    }) { Text("OK") }
                }
            }
        }
    }
}

@Composable
private fun SaturationValuePanel(
    hue: Float,
    sat: Float,
    value: Float,
    onSatValueChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    fun Offset.toSatValue(width: Float, height: Float): Pair<Float, Float> {
        val s = (x / width).coerceIn(0f, 1f)
        val v = 1f - (y / height).coerceIn(0f, 1f)
        return s to v
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val (s, v) = offset.toSatValue(
                        size.width.toFloat(), size.height.toFloat(),
                    )
                    onSatValueChange(s, v)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val (s, v) = change.position.toSatValue(
                        size.width.toFloat(), size.height.toFloat(),
                    )
                    onSatValueChange(s, v)
                }
            },
    ) {
        val pureHue = hsvToColor(hue, 1f, 1f)

        // White → pure hue (saturation axis)
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.White, pureHue),
            )
        )
        // Transparent → black (value axis)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
            )
        )

        // Selection indicator
        val cx = sat * size.width
        val cy = (1f - value) * size.height
        drawCircle(Color.White, 10f, Offset(cx, cy), style = Stroke(3f))
        drawCircle(Color.Black, 12f, Offset(cx, cy), style = Stroke(1.5f))
    }
}

@Composable
private fun HueBar(
    hue: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hueColors = remember {
        (0..360 step 30).map { h ->
            hsvToColor(h.toFloat(), 1f, 1f)
        }
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onHueChange(
                        (offset.y / size.height * 360f).coerceIn(0f, 360f)
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    onHueChange(
                        (change.position.y / size.height * 360f)
                            .coerceIn(0f, 360f)
                    )
                }
            },
    ) {
        drawRect(brush = Brush.verticalGradient(hueColors))

        // Hue position indicator
        val y = hue / 360f * size.height
        drawLine(
            Color.White, Offset(0f, y), Offset(size.width, y),
            strokeWidth = 3f,
        )
    }
}
