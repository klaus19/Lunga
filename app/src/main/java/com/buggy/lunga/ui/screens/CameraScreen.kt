package com.buggy.lunga.ui.screens


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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.buggy.lunga.ui.components.CameraPermissionScreen
import com.buggy.lunga.ui.components.CameraPreview
import com.buggy.lunga.ui.components.RecognizedTextBottomSheet

@OptIn(ExperimentalPermissionsApi::class)  // ← Removed ExperimentalGetImage
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel()
) {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    when {
        cameraPermissionState.status.isGranted -> {
            val isProcessing by viewModel.isProcessing.collectAsState()
            val recognizedText by viewModel.recognizedText.collectAsState()
            val showResult by viewModel.showResult.collectAsState()
            val error by viewModel.error.collectAsState()

            var shouldCapture by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            Box(modifier = Modifier.fillMaxSize()) {
                // Camera Preview
                CameraPreview(
                    onImageCaptured = { imageProxy ->
                        Log.d("CameraScreen", "Image captured, processing...")
                        viewModel.recognizeText(imageProxy)  // ← No annotation issues now
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

                // Results Bottom Sheet
                if (showResult) {
                    RecognizedTextBottomSheet(
                        text = recognizedText,
                        onDismiss = { viewModel.clearResults() },
                        onTranslate = {
                            viewModel.clearResults()
                        }
                    )
                }

                // Error Handling
                error?.let { errorText ->
                    LaunchedEffect(errorText) {
                        Log.e("CameraScreen", "ML Kit error: $errorText")
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