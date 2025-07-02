package com.buggy.lunga.ui.components

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    onImageCaptured: (ImageProxy) -> Unit,
    onError: (Exception) -> Unit,
    shouldCapture: Boolean,
    onCaptureComplete: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    // Create camera executor
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // Dispose executor when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )

                    Log.d("CameraPreview", "Camera bound successfully")

                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Camera binding failed", exc)
                    onError(exc)
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    // Handle capture trigger
    LaunchedEffect(shouldCapture) {
        if (shouldCapture) {
            Log.d("CameraPreview", "Capture triggered")

            val capture = imageCapture
            if (capture != null) {
                Log.d("CameraPreview", "Taking picture...")

                capture.takePicture(
                    cameraExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            Log.d("CameraPreview", "Image captured successfully")
                            onImageCaptured(image)
                            onCaptureComplete()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CameraPreview", "Image capture failed: ${exception.message}")
                            onError(exception)
                            onCaptureComplete()
                        }
                    }
                )
            } else {
                Log.e("CameraPreview", "ImageCapture is null!")
                onError(Exception("Camera not ready"))
                onCaptureComplete()
            }
        }
    }
}