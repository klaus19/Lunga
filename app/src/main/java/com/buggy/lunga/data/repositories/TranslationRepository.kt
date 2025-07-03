package com.buggy.lunga.data.repositories

import android.util.Log
import com.buggy.lunga.data.api.TranslationApiService
import com.buggy.lunga.data.models.Language
import com.buggy.lunga.data.models.TranslationResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TranslationRepository {

    private val translationApi: TranslationApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.mymemory.translated.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApiService::class.java)
    }

    suspend fun translateText(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): Result<TranslationResult> {
        return try {
            if (text.isBlank()) {
                return Result.failure(Exception("Text is empty"))
            }

            if (sourceLanguage.code == targetLanguage.code) {
                // Same language, no translation needed
                return Result.success(
                    TranslationResult(
                        originalText = text,
                        translatedText = text,
                        sourceLanguage = sourceLanguage,
                        targetLanguage = targetLanguage,
                        confidence = 1.0f
                    )
                )
            }

            val languagePair = "${sourceLanguage.code}|${targetLanguage.code}"
            Log.d("Translation", "Translating: $languagePair")

            val response = translationApi.translateText(text, languagePair)

            if (response.isSuccessful && response.body() != null) {
                val translationResponse = response.body()!!

                if (translationResponse.responseStatus == 200) {
                    val translatedText = translationResponse.responseData.translatedText
                    val confidence = translationResponse.responseData.match.toFloat()

                    Log.d("Translation", "Translation successful: $translatedText")

                    Result.success(
                        TranslationResult(
                            originalText = text,
                            translatedText = translatedText,
                            sourceLanguage = sourceLanguage,
                            targetLanguage = targetLanguage,
                            confidence = confidence
                        )
                    )
                } else {
                    Result.failure(Exception("Translation failed: ${translationResponse.responseDetails}"))
                }
            } else {
                Result.failure(Exception("API request failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("Translation", "Error translating text", e)
            Result.failure(e)
        }
    }
}