package com.colorclock.app.data

data class ClockSettings(
    // Schedule
    val dayStartHour: Int = 7,
    val dayStartMinute: Int = 0,
    val nightStartHour: Int = 20,
    val nightStartMinute: Int = 0,

    // Day appearance
    val dayBackgroundColor: Long = 0xFFFFF9C4,  // light yellow
    val dayDigitColor: Long = 0xFF2E7D32,       // green
    val dayBrightness: Float = 0.8f,

    // Night appearance
    val nightBackgroundColor: Long = 0xFF1A237E, // deep blue
    val nightDigitColor: Long = 0xFFD32F2F,      // dim red
    val nightBrightness: Float = 0.05f,

    // Format
    val use24HourFormat: Boolean = false,
)

fun ClockSettings.isNightTime(hour: Int, minute: Int): Boolean {
    val now = hour * 60 + minute
    val dayStart = dayStartHour * 60 + dayStartMinute
    val nightStart = nightStartHour * 60 + nightStartMinute

    return if (dayStart <= nightStart) {
        // Normal: day 07:00, night 20:00 -> night is [20:00, 07:00)
        now >= nightStart || now < dayStart
    } else {
        // Inverted: day 20:00, night 07:00 -> night is [07:00, 20:00)
        now >= nightStart && now < dayStart
    }
}
