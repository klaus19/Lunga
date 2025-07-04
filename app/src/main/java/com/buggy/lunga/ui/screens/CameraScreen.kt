package com.buggy.lunga.ui.screens


import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.buggy.lunga.ui.components.CameraPermissionScreen
import com.buggy.lunga.ui.components.CameraPreview
import com.buggy.lunga.ui.components.RecognizedTextBottomSheet
import com.buggy.lunga.ui.components.TranslationBottomSheet

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val viewModel: CameraViewModel = viewModel {
        CameraViewModel(context.applicationContext as Application)
    }
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    when {
        cameraPermissionState.status.isGranted -> {
            // Collect all states
            val isProcessing by viewModel.isProcessing.collectAsState()
            val recognizedText by viewModel.recognizedText.collectAsState()
            val showResult by viewModel.showResult.collectAsState()
            val error by viewModel.error.collectAsState()

            // Translation states
            val detectedLanguage by viewModel.detectedLanguage.collectAsState()
            val selectedTargetLanguage by viewModel.selectedTargetLanguage.collectAsState()
            val translationResult by viewModel.translationResult.collectAsState()
            val isTranslating by viewModel.isTranslating.collectAsState()
            val showTranslationSheet by viewModel.showTranslationSheet.collectAsState()

            var shouldCapture by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            Box(modifier = Modifier.fillMaxSize()) {
                // Camera Preview
                CameraPreview(
                    onImageCaptured = { imageProxy ->
                        Log.d("CameraScreen", "Image captured, processing...")
                        viewModel.recognizeText(imageProxy)
                    },
                    onError = { exception ->
                        Log.e("CameraScreen", "Camera error: ${exception.message}")
                        errorMessage = "Camera error: ${exception.message}"
                    },
                    shouldCapture = shouldCapture,
                    onCaptureComplete = {
                        shouldCapture = false
                    }
                )

                // Processing Overlay
                if (isProcessing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Recognizing text...",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                // Simple Results Bottom Sheet (shows first)
                if (showResult && !showTranslationSheet) {
                    RecognizedTextBottomSheet(
                        text = recognizedText,
                        onDismiss = { viewModel.clearResults() },
                        onTranslate = {
                            viewModel.showTranslationSheet() // Show translation sheet
                        }
                    )
                }

                // Translation Bottom Sheet (shows when user clicks translate)
                if (showTranslationSheet) {
                    TranslationBottomSheet(
                        recognizedText = recognizedText,
                        detectedLanguage = detectedLanguage,
                        selectedTargetLanguage = selectedTargetLanguage,
                        translationResult = translationResult,
                        isTranslating = isTranslating,
                        onTargetLanguageChanged = { language ->
                            viewModel.setTargetLanguage(language)
                        },
                        onTranslate = {
                            viewModel.translateText()
                        },
                        onSwapLanguages = {
                            viewModel.swapLanguages()
                        },
                        onSaveToHistory = {
                            viewModel.saveTranslationToHistory()
                        },
                        onDismiss = {
                            viewModel.clearResults()
                        }
                    )
                }

                // Error Handling
                error?.let { errorText ->
                    LaunchedEffect(errorText) {
                        Log.e("CameraScreen", "Error: $errorText")
                        viewModel.clearError()
                    }
                }

                errorMessage?.let { errorText ->
                    LaunchedEffect(errorText) {
                        errorMessage = null
                    }
                }

                // Capture Button
                FloatingActionButton(
                    onClick = {
                        Log.d("CameraScreen", "Capture button clicked")
                        shouldCapture = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Capture",
                        tint = Color.White
                    )
                }
            }
        }
        else -> {
            CameraPermissionScreen {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
}