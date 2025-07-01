package com.buggy.lunga.ui.screens

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.buggy.lunga.ui.components.CameraPermissionScreen
import com.buggy.lunga.ui.components.CameraPreview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    when {
        cameraPermissionState.status.isGranted -> {
            CameraContent()
        }
        else -> {
            CameraPermissionScreen {
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
}

@Composable
private fun CameraContent() {
    var capturedImage by remember { mutableStateOf<ImageProxy?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            onImageCaptured = { image ->
                capturedImage = image
            },
            onError = { exception ->
                // Handle error
                Log.e("Camera", "Error: ${exception.message}")
            }
        )

        // Capture Button
        FloatingActionButton(
            onClick = {
                // TODO: Trigger image capture
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "Capture"
            )
        }
    }
}