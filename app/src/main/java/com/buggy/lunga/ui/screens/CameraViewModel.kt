package com.buggy.lunga.ui.screens

import android.app.Application
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.buggy.lunga.data.database.AppDatabase
import com.buggy.lunga.data.models.Language
import com.buggy.lunga.data.models.TranslationResult
import com.buggy.lunga.data.repositories.HistoryRepository
import com.buggy.lunga.data.repositories.LanguageDetectionRepository
import com.buggy.lunga.data.repositories.TextRecognitionRepository
import com.buggy.lunga.data.repositories.TranslationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val textRecognitionRepository = TextRecognitionRepository()
    private val languageDetectionRepository = LanguageDetectionRepository()
    private val translationRepository = TranslationRepository()
    private val historyRepository by lazy {
        HistoryRepository(AppDatabase.getDatabase(application).translationDao())
    }

    // Text Recognition States
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean> = _showResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Language Detection States
    private val _detectedLanguage = MutableStateFlow<Language?>(null)
    val detectedLanguage: StateFlow<Language?> = _detectedLanguage.asStateFlow()

    private val _isDetectingLanguage = MutableStateFlow(false)
    val isDetectingLanguage: StateFlow<Boolean> = _isDetectingLanguage.asStateFlow()

    // Translation States
    private val _selectedTargetLanguage = MutableStateFlow(
        Language("hi", "Hindi", "हिन्दी", "🇮🇳") // Default to Hindi for Indian users
    )
    val selectedTargetLanguage: StateFlow<Language> = _selectedTargetLanguage.asStateFlow()

    private val _translationResult = MutableStateFlow<TranslationResult?>(null)
    val translationResult: StateFlow<TranslationResult?> = _translationResult.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    private val _showTranslationSheet = MutableStateFlow(false)
    val showTranslationSheet: StateFlow<Boolean> = _showTranslationSheet.asStateFlow()

    fun recognizeText(imageProxy: ImageProxy) {
        viewModelScope.launch {
            Log.d("CameraViewModel", "Starting text recognition...")
            _isProcessing.value = true
            _error.value = null

            textRecognitionRepository.recognizeText(imageProxy)
                .onSuccess { text ->
                    Log.d("CameraViewModel", "Text recognition successful: $text")
                    _isProcessing.value = false
                    _recognizedText.value = text
                    _showResult.value = true

                    // Auto-detect language after text recognition
                    if (text.isNotBlank()) {
                        detectLanguage(text)
                    }
                }
                .onFailure { exception ->
                    Log.e("CameraViewModel", "Text recognition failed: ${exception.message}")
                    _isProcessing.value = false
                    _error.value = exception.message ?: "Unknown error occurred"
                }
        }
    }

    private fun detectLanguage(text: String) {
        viewModelScope.launch {
            Log.d("CameraViewModel", "Starting language detection...")
            _isDetectingLanguage.value = true

            languageDetectionRepository.detectLanguage(text)
                .onSuccess { language ->
                    Log.d("CameraViewModel", "Language detected: ${language.name}")
                    _detectedLanguage.value = language
                    _isDetectingLanguage.value = false
                }
                .onFailure { exception ->
                    Log.e("CameraViewModel", "Language detection failed: ${exception.message}")
                    _isDetectingLanguage.value = false
                    // Set default language if detection fails
                    _detectedLanguage.value = Language("en", "English", "English", "🇺🇸")
                }
        }
    }

    fun translateText() {
        val text = _recognizedText.value
        val sourceLanguage = _detectedLanguage.value
        val targetLanguage = _selectedTargetLanguage.value

        if (text.isBlank() || sourceLanguage == null) {
            _error.value = "Cannot translate: missing text or source language"
            return
        }

        viewModelScope.launch {
            Log.d("CameraViewModel", "Starting translation from ${sourceLanguage.name} to ${targetLanguage.name}")
            _isTranslating.value = true

            translationRepository.translateText(text, sourceLanguage, targetLanguage)
                .onSuccess { result ->
                    Log.d("CameraViewModel", "Translation successful: ${result.translatedText}")
                    _translationResult.value = result
                    _isTranslating.value = false
                }
                .onFailure { exception ->
                    Log.e("CameraViewModel", "Translation failed: ${exception.message}")
                    _isTranslating.value = false
                    _error.value = "Translation failed: ${exception.message}"
                }
        }
    }

    fun setTargetLanguage(language: Language) {
        _selectedTargetLanguage.value = language
        // Clear previous translation when language changes
        _translationResult.value = null
    }

    fun swapLanguages() {
        val currentSource = _detectedLanguage.value
        val currentTarget = _selectedTargetLanguage.value

        if (currentSource != null) {
            _selectedTargetLanguage.value = currentSource
            _detectedLanguage.value = currentTarget
            // Clear previous translation when languages are swapped
            _translationResult.value = null
        }
    }

    fun showTranslationSheet() {
        _showTranslationSheet.value = true
        _showResult.value = false // Hide the simple result sheet
    }

    fun saveTranslationToHistory() {
        val result = _translationResult.value
        if (result != null) {
            viewModelScope.launch {
                historyRepository.saveTranslation(result)
                    .onSuccess {
                        Log.d("CameraViewModel", "Translation saved to history successfully")
                        // Optionally show success feedback
                    }
                    .onFailure { exception ->
                        Log.e("CameraViewModel", "Failed to save translation to history", exception)
                        _error.value = "Failed to save to history: ${exception.message}"
                    }
            }
        }
    }

    // ✅ FIXED: These methods were missing
    fun clearResults() {
        _recognizedText.value = ""
        _showResult.value = false
        _showTranslationSheet.value = false
        _error.value = null
        _detectedLanguage.value = null
        _translationResult.value = null
        _isDetectingLanguage.value = false
        _isTranslating.value = false
    }

    fun clearError() {
        _error.value = null
    }
}