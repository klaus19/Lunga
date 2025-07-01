package com.buggy.lunga.ui.screens

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.buggy.lunga.ui.components.CameraPreview

@ExperimentalGetImage
@Composable
private fun CameraContent(
    viewModel: CameraViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var shouldCapture by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        CameraPreview(
            onImageCaptured = { imageProxy ->
                viewModel.recognizeText(imageProxy)
            },
            onError = { exception ->
                errorMessage = "Camera error: ${exception.message}"
            },
            shouldCapture = shouldCapture,
            onCaptureComplete = {
                shouldCapture = false
            }
        )

        // Processing Overlay
        if (uiState.isProcessing) {
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
//        if (uiState.showResult) {
//            RecognizedTextBottomSheet(
//                text = uiState.recognizedText,
//                onDismiss = { viewModel.clearResults() },
//                onTranslate = {
//                    // TODO: Implement translation in next phase
//                    viewModel.clearResults()
//                }
//            )
//        }

        // Error Handling for ML Kit
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // You can show a snackbar here
                viewModel.clearError()
            }
        }

        // Error Handling for Camera
        errorMessage?.let { error ->
            LaunchedEffect(error) {
                // Handle camera error - you can show a snackbar
                errorMessage = null
            }
        }

        // Capture Button
        FloatingActionButton(
            onClick = {
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