package com.colorclock.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.colorclock.app.data.ClockSettings
import com.colorclock.app.data.SettingsRepository
import com.colorclock.app.data.isNightTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

data class ClockState(
    val hour: Int = 0,
    val minute: Int = 0,
    val isNight: Boolean = false,
    val displayTime: String = "",
    val amPm: String = "",
)

class ClockViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SettingsRepository(application)

    val settings: StateFlow<ClockSettings> = repo.settingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, ClockSettings())

    private val _clockState = MutableStateFlow(ClockState())
    val clockState: StateFlow<ClockState> = _clockState.asStateFlow()

    init {
        // Tick every second to catch minute changes promptly
        viewModelScope.launch {
            while (true) {
                updateTime()
                delay(1000)
            }
        }

        // Re-evaluate when settings change
        viewModelScope.launch {
            settings.collect { updateTime() }
        }
    }

    private fun updateTime() {
        val cal = Calendar.getInstance()
        val hour24 = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val s = settings.value
        val isNight = s.isNightTime(hour24, minute)

        val displayTime: String
        val amPm: String
        if (s.use24HourFormat) {
            displayTime = "%02d:%02d".format(hour24, minute)
            amPm = ""
        } else {
            val hour12 = when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }
            displayTime = "%d:%02d".format(hour12, minute)
            amPm = if (hour24 < 12) "AM" else "PM"
        }

        _clockState.value = ClockState(
            hour = hour24,
            minute = minute,
            isNight = isNight,
            displayTime = displayTime,
            amPm = amPm,
        )
    }

    fun updateSettings(newSettings: ClockSettings) {
        viewModelScope.launch {
            repo.save(newSettings)
        }
    }
}
