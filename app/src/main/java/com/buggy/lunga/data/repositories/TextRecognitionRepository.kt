package com.buggy.lunga.data.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TextRecognitionRepository {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun recognizeText(imageProxy: ImageProxy): Result<String> {
        return try {
            // Convert ImageProxy to Bitmap without using experimental API
            val bitmap = imageProxyToBitmap(imageProxy)
            val image = InputImage.fromBitmap(bitmap, 0)

            val result = recognizer.process(image).await()
            val recognizedText = result.text

            Log.d("TextRecognition", "Recognized text: $recognizedText")
            Result.success(recognizedText)
        } catch (e: Exception) {
            Log.e("TextRecognition", "Error recognizing text", e)
            Result.failure(e)
        } finally {
            imageProxy.close()
        }
    }

    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val buffer: ByteBuffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

// Extension function to convert Task to suspend function
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            if (task.isCanceled) {
                cont.cancel()
            } else if (task.isSuccessful) {
                cont.resume(task.result)
            } else {
                cont.resumeWithException(task.exception ?: RuntimeException("Task failed"))
            }
        }
    }
}