package com.colorclock.app.data

data class ClockSettings(
    // Schedule
    val dayStartHour: Int = 7,
    val dayStartMinute: Int = 0,
    val nightStartHour: Int = 20,
    val nightStartMinute: Int = 0,

    // Day appearance
    val dayBackgroundColor: Long = 0xFFFFF9C4,
    val dayDigitColor: Long = 0xFF2E7D32,
    val dayBrightness: Float = 0.8f,
    val dayBgPresets: List<Long> = defaultDayBgPresets,
    val dayDigitPresets: List<Long> = defaultDayDigitPresets,

    // Night appearance
    val nightBackgroundColor: Long = 0xFF1A237E,
    val nightDigitColor: Long = 0xFFD32F2F,
    val nightBrightness: Float = 0.05f,
    val nightBgPresets: List<Long> = defaultNightBgPresets,
    val nightDigitPresets: List<Long> = defaultNightDigitPresets,

    // Format
    val use24HourFormat: Boolean = false,
)

val defaultDayBgPresets = listOf(
    0xFFFFF9C4L, // light yellow
    0xFFC8E6C9L, // light green
    0xFFBBDEFBL, // light blue
    0xFFFFFFFFL, // white
    0xFFFFE0B2L, // light orange
)

val defaultDayDigitPresets = listOf(
    0xFF2E7D32L, // green
    0xFF1565C0L, // blue
    0xFF000000L, // black
    0xFF00695CL, // teal
    0xFF6A1B9AL, // purple
)

val defaultNightBgPresets = listOf(
    0xFF1A237EL, // deep blue
    0xFF4A148CL, // deep purple
    0xFF000000L, // black
    0xFF212121L, // dark gray
    0xFF0D47A1L, // dark blue
)

val defaultNightDigitPresets = listOf(
    0xFFD32F2FL, // dim red
    0xFFE65100L, // dim orange
    0xFF64B5F6L, // light blue
    0xFFA5D6A7L, // light green
    0xFFCE93D8L, // light purple
)

fun ClockSettings.isNightTime(hour: Int, minute: Int): Boolean {
    val now = hour * 60 + minute
    val dayStart = dayStartHour * 60 + dayStartMinute
    val nightStart = nightStartHour * 60 + nightStartMinute

    return if (dayStart <= nightStart) {
        now >= nightStart || now < dayStart
    } else {
        now >= nightStart && now < dayStart
    }
}
