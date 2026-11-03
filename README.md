# 📄 Resume Builder Android App

A modern, native Android application built with **Kotlin**, **Jetpack Compose**, and **Dagger Hilt** that allows users to create, preview, and export professional resumes as formatted PDFs. Integrated with **Firebase** for cloud data persistence and authentication, following clean **MVVM architecture**.

---

## 🚀 Features

- **Intuitive UI**: Built entirely with Jetpack Compose and Material 3 guidelines.
- **Smart Form UX**: Soft keyboard `Next`/`Done` focus navigation and auto-card generation.
- **Advanced Input Components**: Autocomplete Skill Picker with interactive chip tags.
- **Comprehensive Sections**: Personal details, Career Objective, Education with year validations, Work Experience, Projects, Certifications with "No Expiry" toggles, Languages with proficiency levels, and Achievements.
- **Native PDF Export**: Generates professionally styled PDF documents using Android's native `PdfDocument` and Canvas API, matching professional resume templates (`resume_template.docx`).
- **Cloud Integration**: Firebase Authentication, Realtime Database, and Cloud Storage.

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) & Material 3
- **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/) & KSP
- **Architecture**: Clean Feature-Based MVVM (Model-View-ViewModel)
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
│   ├── theme/                  # Material 3 Color, Theme, and Typography
│   └── utils/                  # PDFUtils, ImageUtils, BitmapExtensions
├── data/                       # Data layer
│   ├── model/                  # Data classes (ResumeData, UiState, Certificates, etc.)
│   └── repository/             # ResumeRepository
├── feature/                    # Feature-based presentation layer
│   ├── auth/                   # Login & Signup screens
│   ├── editor/                 # Personal, Education, Skills, Experience, etc.
│   ├── preview/                # PreviewScreen & PDF/Image export
│   └── viewmodel/              # ResumeViewModel (HiltViewModel)
├── navigation/                 # Navigation graph and routes (NavRoutes.kt)
├── MainActivity.kt             # Entry activity (@AndroidEntryPoint)
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
   ```bash
   git clone https://github.com/YOUR_USERNAME/ResumeBuilderApp.git
   ```
2. Open the project in Android Studio.
3. Add your `google-services.json` file inside the `app/` directory (required for Firebase integration).
4. Sync project with Gradle/JDK set to **Java 17**.
5. Run the app on an Android emulator or physical device.

---

## 📄 License

This project is open-source and available under the [LICENSE](LICENSE).
