package com.buggy.lunga.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {

    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAllTranslations(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteTranslations(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE id = :id")
    suspend fun getTranslationById(id: String): TranslationEntity?

    @Query("""
        SELECT * FROM translations 
        WHERE originalText LIKE :query 
           OR translatedText LIKE :query 
           OR sourceLanguageName LIKE :query 
           OR targetLanguageName LIKE :query 
        ORDER BY timestamp DESC
    """)
    fun searchTranslations(query: String): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE sourceLanguageCode = :languageCode OR targetLanguageCode = :languageCode ORDER BY timestamp DESC")
    fun getTranslationsByLanguage(languageCode: String): Flow<List<TranslationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity)

    @Update
    suspend fun updateTranslation(translation: TranslationEntity)

    @Delete
    suspend fun deleteTranslation(translation: TranslationEntity)

    @Query("DELETE FROM translations WHERE id = :id")
    suspend fun deleteTranslationById(id: String)

    @Query("DELETE FROM translations")
    suspend fun deleteAllTranslations()

    @Query("UPDATE translations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM translations")
    suspend fun getTranslationCount(): Int

    @Query("SELECT COUNT(DISTINCT sourceLanguageCode) + COUNT(DISTINCT targetLanguageCode) FROM translations")
    suspend fun getUniqueLanguageCount(): Int
}