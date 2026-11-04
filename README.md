# 📄 Resume Builder Android App

A modern, native Android application built with **Kotlin**, **Jetpack Compose**, and **Dagger Hilt** featuring a robust **Offline-First Architecture (Room Database + WorkManager)**. Allows users to create, preview, and export professional resumes as formatted PDFs, with automatic background synchronization to **Firebase**.

---

## 🚀 Features

- **Offline-First Capabilities**: Instantly saves resume data locally using Room Database so it persists even when offline or when the app is closed.
- **Background Cloud Sync**: Automatically syncs local changes to Firebase Realtime Database and Cloud Storage in the background using Android's WorkManager when a network connection is available.
- **Intuitive UI**: Built entirely with Jetpack Compose and Material 3 guidelines, complete with a slide-out Navigation Drawer connecting all screens.
- **Smart Form UX**: Soft keyboard `Next`/`Done` focus navigation and auto-card generation.
- **Advanced Input Components**: Autocomplete Skill Picker with interactive chip tags.
- **Comprehensive Sections**: Personal details, Career Objective, Education with year validations, Work Experience, Projects, Certifications with "No Expiry" toggles, Languages with proficiency levels, and Achievements.
- **Native PDF Export**: Generates professionally styled PDF documents using Android's native `PdfDocument` and Canvas API, matching professional resume templates (`resume_template.docx`).

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) & Material 3
- **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/) & KSP
- **Local Database**: [Room Database](https://developer.android.com/training/data-storage/room) & Gson
- **Background Processing**: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- **Architecture**: Clean Feature-Based MVVM with Offline-First Repository Pattern
- **Async & Concurrency**: Kotlin Coroutines & StateFlow
- **PDF Rendering**: Android Graphics `PdfDocument` & Canvas API
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) (`coil-compose`)
- **Backend / BaaS**: [Firebase](https://firebase.google.com/) (Auth, Realtime Database, Storage)
- **Build System**: Gradle 8.13 with Android Gradle Plugin 8.11.2 (Java 17)

---

## 📱 Project Structure

```text
com.example.myresumeapp/
├── core/                       # Shared utilities, theme, and DI modules
│   ├── di/                     # Hilt Modules (AppModule)
│   ├── theme/                  # Material 3 Color, Theme, and Typography
│   └── utils/                  # PDFUtils, ImageUtils, BitmapExtensions
├── data/                       # Data layer
│   ├── local/                  # Room Database, Entity, DAO, and TypeConverters
│   ├── model/                  # Data classes (ResumeData, UiState, Certificates, etc.)
│   ├── repository/             # Offline-First ResumeRepository
│   └── worker/                 # SyncResumeWorker (WorkManager background sync)
├── feature/                    # Feature-based presentation layer
│   ├── auth/                   # Login & Signup screens
│   ├── editor/                 # Personal, Education, Skills, Experience, etc.
│   ├── preview/                # PreviewScreen & PDF/Image export
│   └── viewmodel/              # ResumeViewModel (HiltViewModel)
├── navigation/                 # Navigation graph and routes (NavRoutes.kt)
├── MainActivity.kt             # Entry activity
└── MyResumeAppApplication.kt   # Hilt application container (@HiltAndroidApp)
```

---

## 🏁 Getting Started

### Prerequisites
- Android Studio (Koala / Narwhal or newer recommended)
- JDK 17
- Android SDK (Compile SDK 36, Min SDK 26)

### Installation & Setup
1. Clone the repository:
   (git clone https://github.com/YOUR_USERNAME/ResumeBuilderApp.git)
2. Open the project in Android Studio.
3. Add your `google-services.json` file inside the `app/` directory (required for Firebase integration).
4. Sync project with Gradle/JDK set to **Java 17**.
5. Run the app on an Android emulator or physical device.

---

## 📄 License

This project is open-source and available under the [LICENSE](LICENSE).
