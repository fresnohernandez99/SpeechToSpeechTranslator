# Speech-to-Speech Translator (KMP) 🎙️➡️🗣️

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.11.1-purple.svg?style=flat&logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-mpp/)
[![AGP](https://img.shields.io/badge/AGP-9.2.1-green.svg?style=flat&logo=android)](https://developer.android.com/studio/releases/gradle-plugin)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-orange.svg?style=flat)](#)

A high-performance, cross-platform mobile application built with **Kotlin Multiplatform** and **Compose Multiplatform**. The project leverages industry-standard libraries and cutting-edge versions to provide a seamless translation experience.

> **Current Status:** The project is currently a robust **Speech-to-Text (STT)** and **Text-to-Text** translator. The **Text-to-Speech (TTS)** integration is under active development to complete the Speech-to-Speech loop.

---

## ✨ Features

- **Real-time Transcription:** Powered by `Whisper.cpp` via Kotlin Native CInterop for high-accuracy, on-device STT.
- **Multilingual Support:** Translation between dozens of languages using Google ML Kit.
- **True Multiplatform:** Shared UI and logic between Android and iOS using Compose Multiplatform.
- **Modern Architecture:** Clean Architecture principles with MVVM and Dependency Injection.
- **Offline Capable:** Designed to handle heavy lifting (transcription/translation) on-device where possible.

---

## 🛠 Tech Stack

| Category | Technology |
| --- | --- |
| **Language** | Kotlin 2.4.0 |
| **UI Framework** | Compose Multiplatform 1.11.1 |
| **Dependency Injection** | Koin 4.2.2 |
| **Networking** | Ktor 3.5.0 |
| **Local Database** | Room 2.8.4 (KMP) |
| **Image Loading** | Coil 3.5.0 |
| **Build System** | Gradle 9.6.0 + AGP 9.2.1 |
| **Native Integration** | Whisper.cpp (via CInterop) |

---

## 📂 Project Structure

Following the latest **AGP 9.0+** requirements, the project is modularized to separate application entry points from multiplatform logic:

- `composeApp/`: The Android application module (Entry point).
- `sharedUI/`: A KMP library containing 95% of the codebase (Compose UI, ViewModels, Repositories, and Native Interop).
- `lib/`: Android-specific library for native Whisper bindings.
- `iosApp/`: The Xcode project for the iOS target.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio **Ladybug Feature Drop** or higher.
- Xcode 15.0+ (for iOS development).
- Kotlin Multiplatform Mobile plugin installed.

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SpeechToSpeechTranslator.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle (The project uses Kotlin 2.4.0 and AGP 9.2.1, ensure your environment is compatible).
4. For iOS:
   - Navigate to the `iosApp` folder.
   - Run `pod install`.
   - Open `iosApp.xcworkspace` in Xcode.

---

## 🗺 Roadmap

- [x] **Phase 1:** Core KMP Infrastructure & DI Setup.
- [x] **Phase 2:** Whisper.cpp CInterop integration for Android & iOS.
- [x] **Phase 3:** Speech-to-Text & Translation logic.
- [ ] **Phase 4:** **Text-to-Speech (TTS) Integration** (Current Goal 🎯).
- [ ] **Phase 5:** Real-time continuous translation mode.
- [ ] **Phase 6:** UI/UX Polish & Material 3 dynamic theming.

---

## Design Credits
This project uses design assets from the Figma Community file:
[Google Translate Redesign](https://www.figma.com/community/file/874980884980495556)  
Created by [Erhan Kbekar](https://www.figma.com/@ekbekar).

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an issue for feature requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Developed with ❤️ by fresnohernandez99.*
