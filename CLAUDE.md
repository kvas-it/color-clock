# Color Clock

## Project overview

Android app (Kotlin + Jetpack Compose) that displays a full-screen color clock
for kids. The screen color signals whether it's daytime (okay to get up) or
nighttime (stay in bed). Parents configure colors, brightness, and schedule.

## Tech stack

- Kotlin 1.9, Jetpack Compose with Material 3
- Single-activity architecture with Compose Navigation
- DataStore Preferences for persistence
- AndroidViewModel for state management
- Nunito Bold font (bundled TTF from Google Fonts)
- No external dependencies beyond AndroidX

## Project structure

```
app/src/main/java/com/colorclock/app/
  MainActivity.kt          -- wake lock, immersive mode, brightness control
  ClockViewModel.kt        -- clock tick, day/night logic, settings state
  data/
    ClockSettings.kt       -- data model + isNightTime() + default presets
    SettingsRepository.kt  -- DataStore persistence
  ui/
    ClockScreen.kt         -- full-screen clock with auto-hiding settings gear
    SettingsScreen.kt      -- all config: schedule, colors, brightness, format
    HsvColorPicker.kt      -- HSV color picker (hue bar + SV square)
    Navigation.kt          -- two-screen nav (clock <-> settings)
    theme/
      Color.kt             -- Long.toComposeColor() extension
      Theme.kt             -- Material 3 dark theme wrapper
```

## Key design decisions

- Brightness is per-window (`screenBrightness`), no system permission needed
- Color presets: 5 editable swatches per color slot, long-press to open HSV picker
- Time picker uses `TimeInput` (not `TimePicker` dial) to fit landscape layout
- Settings gear auto-hides after 4 seconds, tap screen to show it

## Building

Open in Android Studio, sync Gradle, run on emulator or device.
Debug APK: Build > Build Bundle(s) / APK(s) > Build APK(s).

## Future ideas

- Gradual color transition between day/night
- Seconds display (optional)
- Wake-up alarm/chime
- Weekday/weekend schedules
- Font selection
- Custom app icon
