package com.buggy.lunga.ui.screens

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buggy.lunga.data.repositories.TextRecognitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CameraViewModel(
    private val textRecognitionRepository: TextRecognitionRepository = TextRecognitionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    @ExperimentalGetImage
    fun recognizeText(imageProxy: ImageProxy) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true)

            textRecognitionRepository.recognizeText(imageProxy)
                .onSuccess { recognizedText ->
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        recognizedText = recognizedText
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        error = exception.message
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class CameraUiState(
    val isProcessing: Boolean = false,
    val recognizedText: String = "",
    val error: String? = null
)