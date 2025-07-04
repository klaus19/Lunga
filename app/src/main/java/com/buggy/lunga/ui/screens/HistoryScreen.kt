package com.buggy.lunga.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.buggy.lunga.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel()
) {
    val translations by viewModel.translations.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState()
    val selectedLanguageFilter by viewModel.selectedLanguageFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val stats by viewModel.stats.collectAsState()

    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Statistics card
            if (stats.totalTranslations > 0) {
                item {
                    HistoryStatsCard(stats = stats)
                }
            }

            // Search and filters
            item {
                HistorySearchAndFilter(
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::setSearchQuery,
                    showFavoritesOnly = showFavoritesOnly,
                    onToggleFavorites = viewModel::toggleFavoritesFilter,
                    selectedLanguageFilter = selectedLanguageFilter,
                    onLanguageFilterChange = viewModel::setLanguageFilter,
                    onClearFilters = {
                        viewModel.setSearchQuery("")
                        viewModel.setLanguageFilter(null)
                        if (showFavoritesOnly) viewModel.toggleFavoritesFilter()
                    }
                )
            }

            // Clear all button (if there are translations)
            if (translations.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { showDeleteAllDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear all",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear All")
                        }
                    }
                }
            }

            // Translation list
            if (translations.isEmpty() && !isLoading) {
                item {
                    EmptyHistoryState(
                        hasFilters = searchQuery.isNotEmpty() ||
                                showFavoritesOnly ||
                                selectedLanguageFilter != null
                    )
                }
            } else {
                items(
                    items = translations,
                    key = { it.id }
                ) { translation ->
                    TranslationHistoryCard(
                        translation = translation,
                        onFavoriteClick = { viewModel.toggleFavorite(translation) },
                        onDeleteClick = { showDeleteDialog = translation.id },
                        onCopyOriginal = { /* Handle copy feedback */ },
                        onCopyTranslation = { /* Handle copy feedback */ },
                        isFavorite = false // You'd need to track this in the entity/viewmodel
                    )
                }
            }
        }

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    // Error snackbar
    error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            viewModel.clearError()
        }
    }

    // Delete confirmation dialogs
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text("Clear All Translations") },
            text = { Text("Are you sure you want to delete all translation history? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllTranslations()
                        showDeleteAllDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    showDeleteDialog?.let { translationId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Translation") },
            text = { Text("Are you sure you want to delete this translation?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTranslation(translationId)
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmptyHistoryState(
    hasFilters: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (hasFilters) Icons.Default.SearchOff else Icons.Default.History,
            contentDescription = if (hasFilters) "No results" else "No history",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (hasFilters) "No matching translations" else "No translation history yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (hasFilters) {
                "Try adjusting your search or filters to find what you're looking for."
            } else {
                "Start translating text to build your history. All your translations will appear here."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}