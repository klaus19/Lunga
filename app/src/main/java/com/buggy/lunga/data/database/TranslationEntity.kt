package com.buggy.lunga.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buggy.lunga.data.models.Language
import com.buggy.lunga.data.models.TranslationResult

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey val id: String,
    val originalText: String,
    val translatedText: String,
    val sourceLanguageCode: String,
    val sourceLanguageName: String,
    val sourceLanguageNativeName: String,
    val sourceLanguageFlag: String,
    val targetLanguageCode: String,
    val targetLanguageName: String,
    val targetLanguageNativeName: String,
    val targetLanguageFlag: String,
    val confidence: Float,
    val timestamp: Long,
    val isFavorite: Boolean = false
) {
    fun toTranslationResult(): TranslationResult {
        return TranslationResult(
            id = id,
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = Language(
                code = sourceLanguageCode,
                name = sourceLanguageName,
                nativeName = sourceLanguageNativeName,
                flag = sourceLanguageFlag
            ),
            targetLanguage = Language(
                code = targetLanguageCode,
                name = targetLanguageName,
                nativeName = targetLanguageNativeName,
                flag = targetLanguageFlag
            ),
            confidence = confidence,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromTranslationResult(result: TranslationResult, isFavorite: Boolean = false): TranslationEntity {
            return TranslationEntity(
                id = result.id,
                originalText = result.originalText,
                translatedText = result.translatedText,
                sourceLanguageCode = result.sourceLanguage.code,
                sourceLanguageName = result.sourceLanguage.name,
                sourceLanguageNativeName = result.sourceLanguage.nativeName,
                sourceLanguageFlag = result.sourceLanguage.flag,
                targetLanguageCode = result.targetLanguage.code,
                targetLanguageName = result.targetLanguage.name,
                targetLanguageNativeName = result.targetLanguage.nativeName,
                targetLanguageFlag = result.targetLanguage.flag,
                confidence = result.confidence,
                timestamp = result.timestamp,
                isFavorite = isFavorite
            )
        }
    }
}