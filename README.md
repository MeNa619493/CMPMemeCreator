# MemeCreator

Cross-platform meme editor built with **Kotlin Multiplatform** and **Compose Multiplatform**. Shared UI and logic run on **Android** and **iOS**; platform-specific pieces handle saving, sharing, and image export.

\---

## Features

* **Gallery** — Browse meme templates and pick one to edit.
* **Editor** — Place and style text on the template, with draggable text boxes and export/share flows backed by expect/actual implementations per platform.

\---

## Demo recordings

Screen recordings of the app running on real devices live in the [`demo/`](./demo) folder:

|Platform|Recording|Description|
|-|-|-|
|**Android**|[android\_demo.mp4](./demo/android_demo.mp4)|Demo captured on an Android device.|
|**iOS**|[ios\_demo.mp4](./demo/ios_demo.mp4)|Demo captured on an iPhone (iOS device).|

On GitHub, open a link to play the video in the browser; locally, open the files with any media player.

\---

## Tech stack

|Area|Choices|
|-|-|
|Language|Kotlin 2.2|
|UI|Compose Multiplatform, Material 3|
|Navigation|JetBrains Navigation for Compose (typed routes)|
|DI|Koin|
|Images|Coil 3|
|Serialization|kotlinx.serialization|

Android targets **minSdk 24**, **compile/target SDK 36** (see `gradle/libs.versions.toml`). iOS deployment target is **17.0** in the Xcode project (`iosApp/iosApp.xcodeproj`).

\---

## 🏗 Architecture

This project follows **MVI (Model-View-Intent)** with clean separation of concerns:

```
User Interaction (Intent)
        ↓
   ViewModel
        ↓
  State Update (Model)
        ↓
   Composable UI (View)
```

* **State** — Immutable data classes that represent the full UI state at any point in time
* **Intent** — Sealed classes representing every possible user action
* **ViewModel** — Processes intents and emits new state via `StateFlow`
* **Composables** — Observe state and render the UI; they only emit intents, never mutate state directly

\---

## Requirements

* **JDK 17+** (typical for current Android Gradle Plugin)
* **Android Studio** (Koala+ recommended) or IntelliJ with KMP plugins
* **Xcode** with an **iOS 17+** simulator runtime (or device) for the iOS app
* **macOS** to build and run the iOS target

\---

## Project layout

```
composeApp/          # KMP module: shared + androidMain + iosMain
iosApp/              # Xcode app shell; Swift entry loads Compose
  iosApp.xcodeproj   # Open this in Xcode
  Configuration/     # Config.xcconfig (bundle id, team placeholder)
```

* **`composeApp/src/commonMain`** — Screens, ViewModel, navigation, shared resources.
* **`composeApp/src/androidMain`** — `MainActivity`, Android exporters, FileProvider-style paths, etc.
* **`composeApp/src/iosMain`** — `MainViewController`, iOS exporters, cache paths, share sheet.

The iOS target builds a static **ComposeApp** framework via Gradle; Xcode runs the **Compile Kotlin Framework** script (`embedAndSignAppleFrameworkForXcode`) before compiling Swift.

\---

## Run on Android

From the repo root:

```bash
./gradlew :composeApp:installDebug
```

Or use Android Studio: open the project root, select the **composeApp** run configuration, choose a device or emulator, then Run.

\---

## Run on iOS

1. Optional: set your Apple team for signing. Edit `iosApp/Configuration/Config.xcconfig` and set `TEAM\_ID` to your team suffix (or configure signing in Xcode). Simulators often work without a paid team; devices need a valid team.
2. Open **`iosApp/iosApp.xcodeproj`** in Xcode (not only the `iosApp` folder).
3. Select the **`iosApp`** scheme and an **iOS Simulator** (or device) whose OS is **≥ 17.0**.
4. Build \& Run (**⌘R**). The first build may take a while while Gradle produces the framework.

If no simulators appear, install an iOS 17+ runtime under **Xcode → Settings → Platforms** and ensure the deployment target in Xcode is not higher than the runtimes you have installed.

\---

## 📸 Screenshots

>

!\[homescreen](https://github.com/user-attachments/assets/6ffd9097-7e8b-4487-84e3-23fc064f674f)
!\[screenshow](https://github.com/user-attachments/assets/c065767f-32e0-4239-932a-3febe1e0818a)
!\[position text](https://github.com/user-attachments/assets/7bea3b7c-edfd-48b5-89ea-82b9d67a7bcd)
!\[expand and contract ](https://github.com/user-attachments/assets/3ae99172-e112-4aaf-a964-d68e654c2b9c)
!\[screenshow](https://github.com/user-attachments/assets/bcac9118-4406-415e-8ba1-88b5b8931e34)

