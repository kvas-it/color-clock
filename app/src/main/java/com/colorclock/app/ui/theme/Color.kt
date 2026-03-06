package com.colorclock.app.ui.theme

import androidx.compose.ui.graphics.Color

// Preset colors for backgrounds
val presetBackgroundColors = listOf(
    0xFFFFF9C4 to "Light Yellow",
    0xFFC8E6C9 to "Light Green",
    0xFFBBDEFB to "Light Blue",
    0xFFFFFFFF to "White",
    0xFFFFE0B2 to "Light Orange",
    0xFFF8BBD0 to "Light Pink",
    0xFFE1BEE7 to "Light Purple",
    0xFFB2DFDB to "Light Teal",
    0xFF1A237E to "Deep Blue",
    0xFF4A148C to "Deep Purple",
    0xFF212121 to "Dark Gray",
    0xFF000000 to "Black",
    0xFF1B5E20 to "Dark Green",
    0xFF311B92 to "Indigo",
    0xFF0D47A1 to "Dark Blue",
    0xFF3E2723 to "Dark Brown",
)

// Preset colors for digits
val presetDigitColors = listOf(
    0xFF2E7D32 to "Green",
    0xFF1565C0 to "Blue",
    0xFF000000 to "Black",
    0xFF4E342E to "Brown",
    0xFF6A1B9A to "Purple",
    0xFF00695C to "Teal",
    0xFFD32F2F to "Red",
    0xFFE65100 to "Orange",
    0xFFFFFFFF to "White",
    0xFFFF6F00 to "Amber",
    0xFF64B5F6 to "Light Blue",
    0xFFA5D6A7 to "Light Green",
    0xFFEF9A9A to "Light Red",
    0xFFCE93D8 to "Light Purple",
    0xFFFFCC80 to "Light Orange",
    0xFF90A4AE to "Gray",
)

fun Long.toComposeColor(): Color = Color(this)
