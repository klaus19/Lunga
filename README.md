# 🌍 LUNGA - An AI-Powered Text Translation App

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg" alt="UI">
  <img src="https://img.shields.io/badge/ML-Google%20ML%20Kit-red.svg" alt="ML">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
</div>

## 📱 Overview

Language Assistant is a modern Android application that uses AI-powered text recognition and translation to help users translate text from images instantly. Simply point your camera at any text, and the app will detect the language and translate it to your preferred language.

## ✨ Features

### 🎯 Current Features
- **📸 Camera Text Recognition** - Capture and extract text from images using Google ML Kit
- **🤖 Automatic Language Detection** - AI-powered language identification
- **🌐 Real-time Translation** - Translate text between 20+ languages
- **📋 Copy to Clipboard** - Easy text copying functionality
- **🎨 Modern UI** - Beautiful Material 3 design with Jetpack Compose
- **🔄 Language Swapping** - Quick source/target language switching
- **📱 Intuitive UX** - Clean, user-friendly interface

### 🚀 Upcoming Features
- **📚 Translation History** - Save and manage translation history
- **⭐ Favorites** - Mark frequently used translations
- **🔊 Text-to-Speech** - Audio pronunciation of translations
- **📤 Share Translations** - Share results with other apps
- **🌙 Dark Mode** - Complete dark theme support
- **🚫 Offline Mode** - Translation without internet connection

## 📸 Screenshots

*Add your app screenshots here*

| Camera View | Text Recognition | Translation Results |
|-------------|------------------|-------------------|
| ![Camera](screenshots/camera.png) | ![Recognition](screenshots/recognition.png) | ![Translation](screenshots/translation.png) |

## 🛠️ Tech Stack

### **Frontend**
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material 3** - Latest Material Design components
- **ViewModel** - MVVM architecture pattern
- **StateFlow** - Reactive state management

### **Camera & ML**
- **CameraX** - Camera functionality and image capture
- **Google ML Kit Text Recognition** - OCR text extraction
- **Google ML Kit Language ID** - Automatic language detection

### **Networking & Translation**
- **Retrofit** - HTTP client for API calls
- **Gson** - JSON serialization/deserialization
- **MyMemory Translation API** - Free translation service

### **Architecture**
- **MVVM** - Model-View-ViewModel pattern
- **Repository Pattern** - Data layer abstraction
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams

## 🚀 Getting Started

### Prerequisites
- **Android Studio** (Latest version recommended)
- **Android SDK** (API level 24+)
- **Kotlin** 1.8+
- **Physical Android device** (for camera functionality)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/klaus19/Lunga
   cd Lunga
