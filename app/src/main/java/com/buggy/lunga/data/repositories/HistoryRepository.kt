package com.buggy.lunga.data.repositories

import android.util.Log
import com.buggy.lunga.data.database.TranslationDao
import com.buggy.lunga.data.database.TranslationEntity
import com.buggy.lunga.data.models.TranslationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepository(private val translationDao: TranslationDao) {

    fun getAllTranslations(): Flow<List<TranslationResult>> {
        return translationDao.getAllTranslations().map { entities ->
            entities.map { it.toTranslationResult() }
        }
    }

    fun getFavoriteTranslations(): Flow<List<TranslationResult>> {
        return translationDao.getFavoriteTranslations().map { entities ->
            entities.map { it.toTranslationResult() }
        }
    }

    fun searchTranslations(query: String): Flow<List<TranslationResult>> {
        val searchQuery = "%$query%"
        return translationDao.searchTranslations(searchQuery).map { entities ->
            entities.map { it.toTranslationResult() }
        }
    }

    fun getTranslationsByLanguage(languageCode: String): Flow<List<TranslationResult>> {
        return translationDao.getTranslationsByLanguage(languageCode).map { entities ->
            entities.map { it.toTranslationResult() }
        }
    }

    suspend fun saveTranslation(translation: TranslationResult): Result<Unit> {
        return try {
            val entity = TranslationEntity.fromTranslationResult(translation)
            translationDao.insertTranslation(entity)
            Log.d("HistoryRepository", "Translation saved: ${translation.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Error saving translation", e)
            Result.failure(e)
        }
    }

    suspend fun deleteTranslation(translationId: String): Result<Unit> {
        return try {
            translationDao.deleteTranslationById(translationId)
            Log.d("HistoryRepository", "Translation deleted: $translationId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Error deleting translation", e)
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(translationId: String, isFavorite: Boolean): Result<Unit> {
        return try {
            translationDao.updateFavoriteStatus(translationId, isFavorite)
            Log.d("HistoryRepository", "Favorite status updated: $translationId -> $isFavorite")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Error updating favorite status", e)
            Result.failure(e)
        }
    }

    suspend fun deleteAllTranslations(): Result<Unit> {
        return try {
            translationDao.deleteAllTranslations()
            Log.d("HistoryRepository", "All translations deleted")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("HistoryRepository", "Error deleting all translations", e)
            Result.failure(e)
        }
    }

    suspend fun getTranslationCount(): Int {
        return translationDao.getTranslationCount()
    }

    suspend fun getUniqueLanguageCount(): Int {
        return translationDao.getUniqueLanguageCount()
    }
}