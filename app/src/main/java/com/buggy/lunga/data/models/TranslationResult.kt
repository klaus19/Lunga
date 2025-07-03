package com.buggy.lunga.data.models

data class TranslationResult(
    val id: String = java.util.UUID.randomUUID().toString(),
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val timestamp: Long = System.currentTimeMillis(),
    val confidence: Float = 0f
)