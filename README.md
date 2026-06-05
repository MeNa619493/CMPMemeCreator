# 🎨 CMP Meme Creator

Cross-platform meme editor built with Kotlin Multiplatform and Compose Multiplatform. Shared UI and logic run on Android and iOS; platform-specific pieces handle saving, sharing, and image export.

## ✨ Features

* 🖼️ Pick any image from your device as a meme base
* ✍️ Add and position custom text overlays
* 🎨 Shared UI across Android and iOS using Jetpack Compose
* 💾 Export and save the finished meme to your device
* ⚡ Reactive state management with Kotlin Coroutines \& Flow

## 🛠 Tech Stack

|Layer|Technology|
|-|-|
|UI|Compose Multiplatform|
|Language|Kotlin (KMP)|
|Architecture|MVI (Model-View-Intent)|
|Dependency Injection|Koin|
|Async|Kotlin Coroutines \& Flow|
|Platforms|Android · iOS|

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

## 📂 Project Structure

```
CMP\\\_MemeCreator/
├── composeApp/               # Shared KMP module (90%+ of the codebase)
│   └── src/
│       ├── commonMain/       # Shared UI, ViewModels, business logic
│       ├── androidMain/      # Android platform-specific implementations
│       └── iosMain/          # iOS platform-specific implementations
├── iosApp/                   # Thin iOS native wrapper (Swift/Xcode)
├── build.gradle.kts          # Root build config
└── settings.gradle.kts       # Module declarations
```

## 📸 Screenshots

!\[](https://raw.githubusercontent.com/MeNa619493/CMPMemeCreator/refs/heads/master/561204491-bcac9118-4406-415e-8ba1-88b5b8931e34.gif)

!\[](https://raw.githubusercontent.com/MeNa619493/CMPMemeCreator/refs/heads/master/561206678-3ae99172-e112-4aaf-a964-d68e654c2b9c.gif)

