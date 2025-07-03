package com.buggy.lunga.data.repositories

import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.buggy.lunga.data.models.Language
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LanguageDetectionRepository {

    private val languageIdentifier = LanguageIdentification.getClient(
        LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(0.34f)
            .build()
    )

    suspend fun detectLanguage(text: String): Result<Language> {
        return try {
            if (text.isBlank()) {
                return Result.failure(Exception("Text is empty"))
            }

            val detectedLanguageCode = languageIdentifier.identifyLanguage(text).awaitTask()

            Log.d("LanguageDetection", "Detected language code: $detectedLanguageCode")

            if (detectedLanguageCode == "und") {
                // "und" means undetermined
                return Result.failure(Exception("Could not determine language"))
            }

            val language = Language.getLanguageByCode(detectedLanguageCode)
                ?: Language("en", "English", "English", "🇺🇸") // Default to English

            Result.success(language)
        } catch (e: Exception) {
            Log.e("LanguageDetection", "Error detecting language", e)
            Result.failure(e)
        }
    }

    suspend fun detectPossibleLanguages(text: String): Result<List<Pair<Language, Float>>> {
        return try {
            if (text.isBlank()) {
                return Result.failure(Exception("Text is empty"))
            }

            val possibleLanguages = languageIdentifier.identifyPossibleLanguages(text).awaitTask()

            val languagesList = possibleLanguages.mapNotNull { identifiedLanguage ->
                val language = Language.getLanguageByCode(identifiedLanguage.languageTag)
                if (language != null) {
                    Pair(language, identifiedLanguage.confidence)
                } else {
                    null
                }
            }

            Result.success(languagesList)
        } catch (e: Exception) {
            Log.e("LanguageDetection", "Error detecting possible languages", e)
            Result.failure(e)
        }
    }
}

// Extension function for ML Kit Task with different name to avoid conflicts
suspend fun <T> com.google.android.gms.tasks.Task<T>.awaitTask(): T {
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