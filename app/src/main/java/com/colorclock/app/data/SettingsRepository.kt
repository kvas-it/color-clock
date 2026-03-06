package com.colorclock.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences>
    by preferencesDataStore(name = "clock_settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val DAY_START_HOUR = intPreferencesKey("day_start_hour")
        val DAY_START_MINUTE = intPreferencesKey("day_start_minute")
        val NIGHT_START_HOUR = intPreferencesKey("night_start_hour")
        val NIGHT_START_MINUTE = intPreferencesKey("night_start_minute")
        val DAY_BG_COLOR = longPreferencesKey("day_bg_color")
        val DAY_DIGIT_COLOR = longPreferencesKey("day_digit_color")
        val DAY_BRIGHTNESS = floatPreferencesKey("day_brightness")
        val NIGHT_BG_COLOR = longPreferencesKey("night_bg_color")
        val NIGHT_DIGIT_COLOR = longPreferencesKey("night_digit_color")
        val NIGHT_BRIGHTNESS = floatPreferencesKey("night_brightness")
        val USE_24_HOUR = booleanPreferencesKey("use_24_hour")
    }

    val settingsFlow: Flow<ClockSettings> = context.dataStore.data.map { prefs ->
        val defaults = ClockSettings()
        ClockSettings(
            dayStartHour = prefs[Keys.DAY_START_HOUR] ?: defaults.dayStartHour,
            dayStartMinute = prefs[Keys.DAY_START_MINUTE] ?: defaults.dayStartMinute,
            nightStartHour = prefs[Keys.NIGHT_START_HOUR] ?: defaults.nightStartHour,
            nightStartMinute = prefs[Keys.NIGHT_START_MINUTE] ?: defaults.nightStartMinute,
            dayBackgroundColor = prefs[Keys.DAY_BG_COLOR] ?: defaults.dayBackgroundColor,
            dayDigitColor = prefs[Keys.DAY_DIGIT_COLOR] ?: defaults.dayDigitColor,
            dayBrightness = prefs[Keys.DAY_BRIGHTNESS] ?: defaults.dayBrightness,
            nightBackgroundColor = prefs[Keys.NIGHT_BG_COLOR]
                ?: defaults.nightBackgroundColor,
            nightDigitColor = prefs[Keys.NIGHT_DIGIT_COLOR]
                ?: defaults.nightDigitColor,
            nightBrightness = prefs[Keys.NIGHT_BRIGHTNESS]
                ?: defaults.nightBrightness,
            use24HourFormat = prefs[Keys.USE_24_HOUR] ?: defaults.use24HourFormat,
        )
    }

    suspend fun save(settings: ClockSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DAY_START_HOUR] = settings.dayStartHour
            prefs[Keys.DAY_START_MINUTE] = settings.dayStartMinute
            prefs[Keys.NIGHT_START_HOUR] = settings.nightStartHour
            prefs[Keys.NIGHT_START_MINUTE] = settings.nightStartMinute
            prefs[Keys.DAY_BG_COLOR] = settings.dayBackgroundColor
            prefs[Keys.DAY_DIGIT_COLOR] = settings.dayDigitColor
            prefs[Keys.DAY_BRIGHTNESS] = settings.dayBrightness
            prefs[Keys.NIGHT_BG_COLOR] = settings.nightBackgroundColor
            prefs[Keys.NIGHT_DIGIT_COLOR] = settings.nightDigitColor
            prefs[Keys.NIGHT_BRIGHTNESS] = settings.nightBrightness
            prefs[Keys.USE_24_HOUR] = settings.use24HourFormat
        }
    }
}
