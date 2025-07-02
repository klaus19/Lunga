package com.buggy.lunga.ui.screens

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buggy.lunga.data.repositories.TextRecognitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    private val textRecognitionRepository = TextRecognitionRepository()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean> = _showResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun recognizeText(imageProxy: ImageProxy) {  // ← No annotation needed
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
                }
                .onFailure { exception ->
                    Log.e("CameraViewModel", "Text recognition failed: ${exception.message}")
                    _isProcessing.value = false
                    _error.value = exception.message ?: "Unknown error occurred"
                }
        }
    }

    fun clearResults() {
        _recognizedText.value = ""
        _showResult.value = false
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }
}