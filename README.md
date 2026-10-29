# 📄 Resume Builder Android App

A modern, native Android application built with **Kotlin** and **Jetpack Compose** that allows users to create, preview, and export professional resumes as formatted PDFs. Integrated with **Firebase** for cloud data persistence and authentication.

---

## 🚀 Features

- **Intuitive UI**: Built entirely with Jetpack Compose and Material 3 guidelines.
- **Comprehensive Sections**:
  - Personal Details & Profile Photo (with Coil integration)
  - Career Objective
  - Education (Higher Education, Senior Secondary, Secondary)
  - Work Experience
  - Projects & Technical Stack
  - Certifications & Associations
  - Key Skills (Categorized into Tech Skills, Soft Skills, and Languages)
  - Achievements & Extracurricular Activities
- **Native PDF Export**: Generates professionally styled PDF documents using Android's native `PdfDocument` and Canvas API, matching clean professional resume templates.
- **Cloud Integration**: Firebase Authentication, Realtime Database, and Cloud Storage for saving and syncing resume data.

---

## 🛠️ Tech Stack & Libraries

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) & Material 3
- **Async & Concurrency**: Kotlin Coroutines & StateFlow
- **PDF Rendering**: Android Graphics `PdfDocument` & Canvas API
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) (`coil-compose`)
- **Backend / BaaS**: [Firebase](https://firebase.google.com/) (Auth, Realtime Database, Storage)
- **Build System**: Gradle 8.13 with Android Gradle Plugin 8.11.2 (Java 17)

---

## 📱 Project Structure

```text
com.example.myresumeapp/
├── data/           # Data models (ResumeData, Experience, Project, etc.) & Repository
├── uiscreens/      # Compose UI Screens (Login, Signup, Resume Editor, Preview)
├── utils/          # PDF Generation & Canvas utilities (PDFUtils.kt)
└── ui/theme/       # Material 3 Theme configuration
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

This project is open-source and available under the [MIT License](LICENSE).
