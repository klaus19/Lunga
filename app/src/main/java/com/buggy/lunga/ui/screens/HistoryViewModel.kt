package com.buggy.lunga.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.buggy.lunga.data.database.AppDatabase
import com.buggy.lunga.data.models.TranslationResult
import com.buggy.lunga.data.repositories.HistoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val historyRepository = HistoryRepository(
        AppDatabase.getDatabase(application).translationDao()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    private val _selectedLanguageFilter = MutableStateFlow<String?>(null)
    val selectedLanguageFilter: StateFlow<String?> = _selectedLanguageFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Combine all filters to get filtered translations
    val translations: StateFlow<List<TranslationResult>> = combine(
        searchQuery,
        showFavoritesOnly,
        selectedLanguageFilter
    ) { query, favoritesOnly, languageFilter ->
        Triple(query, favoritesOnly, languageFilter)
    }.flatMapLatest { (query, favoritesOnly, languageFilter) ->
        when {
            query.isNotBlank() -> historyRepository.searchTranslations(query)
            favoritesOnly -> historyRepository.getFavoriteTranslations()
            languageFilter != null -> historyRepository.getTranslationsByLanguage(languageFilter)
            else -> historyRepository.getAllTranslations()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _stats = MutableStateFlow(HistoryStats())
    val stats: StateFlow<HistoryStats> = _stats.asStateFlow()

    init {
        loadStats()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun setLanguageFilter(languageCode: String?) {
        _selectedLanguageFilter.value = languageCode
    }

    fun toggleFavorite(translation: TranslationResult) {
        viewModelScope.launch {
            val currentFavoriteStatus = translations.value
                .find { it.id == translation.id }
                ?.let { false } // This would need to be tracked differently

            historyRepository.toggleFavorite(translation.id, currentFavoriteStatus != true)
                .onFailure { exception ->
                    _error.value = "Failed to update favorite: ${exception.message}"
                }
        }
    }

    fun deleteTranslation(translationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            historyRepository.deleteTranslation(translationId)
                .onSuccess {
                    loadStats()
                }
                .onFailure { exception ->
                    _error.value = "Failed to delete translation: ${exception.message}"
                }
            _isLoading.value = false
        }
    }

    fun deleteAllTranslations() {
        viewModelScope.launch {
            _isLoading.value = true
            historyRepository.deleteAllTranslations()
                .onSuccess {
                    loadStats()
                }
                .onFailure { exception ->
                    _error.value = "Failed to delete all translations: ${exception.message}"
                }
            _isLoading.value = false
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                val count = historyRepository.getTranslationCount()
                val languageCount = historyRepository.getUniqueLanguageCount()
                _stats.value = HistoryStats(
                    totalTranslations = count,
                    uniqueLanguages = languageCount
                )
            } catch (e: Exception) {
                _error.value = "Failed to load stats: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

data class HistoryStats(
    val totalTranslations: Int = 0,
    val uniqueLanguages: Int = 0
)