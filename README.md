# ProPlayer FF — Free Fire Pro Settings Advisor

Android app (Kotlin + Jetpack Compose + MVVM) that scans your device's real
hardware and recommends optimal Free Fire sensitivity, graphics, and HUD
settings — entirely offline, on-device.

Languages: English, Tajik (Тоҷикӣ), Uzbek (O'zbekcha) — switchable in-app,
no restart required.

## What it actually does

- Reads real device specs (RAM, CPU cores, screen resolution/refresh rate,
  Android version) via public Android APIs — no root, no special permissions.
- Classifies the device into a performance tier (Low / Mid / High / Flagship)
  using a transparent heuristic scoring formula.
- Computes sensitivity values (General, Red Dot, 2x/4x/AWM scopes, Free Look)
  from a baseline profile adjusted by device tier and chosen playstyle
  (Rush / Balanced / Sniper).
- Recommends graphics settings (resolution, quality, target frame rate,
  shadows) tuned to keep FPS stable on that specific device.
- Suggests HUD/control layout tips (fire button size, gyroscope, quick
  reload, claw layout) based on screen size and playstyle.

## Honesty note

This app is an independent fan-made calculator. It cannot read or modify
Free Fire's actual settings — you enter the recommended numbers yourself in
the game. Recommendations are heuristic estimates based on your hardware,
not official Garena values. Not affiliated with or endorsed by Garena.

## Building

```
cd ProPlayerFF
./gradlew assembleDebug   # or: gradle assembleDebug
```
APK output: `app/build/outputs/apk/debug/app-debug.apk`

A GitHub Actions workflow (`.github/workflows/build-apk.yml`) builds the APK
automatically on every push and uploads it as an artifact — useful for
building from a phone without a local Android SDK.

## Architecture

```
app/
├── core/          AppContainer (manual DI), constants
├── domain/
│   ├── model/     DeviceProfile, SensitivitySettings, GraphicsSettings, HudRecommendation
│   └── engine/    DeviceTierClassifier, SensitivityEngine, GraphicsEngine, HudAdvisor
├── data/          DeviceScanner (real Android APIs), PreferencesStore (DataStore)
├── i18n/          Language, Strings (EN/TG/UZ), LocalStrings CompositionLocal
├── presentation/  MainViewModel (MVVM)
└── ui/            Compose screens, navigation, theme, components
```
