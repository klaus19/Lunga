package com.buggy.lunga.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Using MyMemory Translation API (free, no API key required)
interface TranslationApiService {
    @GET("get")
    suspend fun translateText(
        @Query("q") text: String,
        @Query("langpair") languagePair: String
    ): Response<TranslationResponse>
}

data class TranslationResponse(
    val responseData: ResponseData,
    val responseStatus: Int,
    val responseDetails: String?,
    val matches: List<Match>?
)

data class ResponseData(
    val translatedText: String,
    val match: Double
)

data class Match(
    val id: String,
    val segment: String,
    val translation: String,
    val source: String,
    val target: String,
    val quality: String,
    val reference: String?,
    val usageCount: Int,
    val subject: String,
    val createdBy: String,
    val lastUpdatedBy: String,
    val createDate: String,
    val lastUpdateDate: String,
    val match: Double
)