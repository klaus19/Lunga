package com.buggy.lunga.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.buggy.lunga.data.models.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySearchAndFilter(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showFavoritesOnly: Boolean,
    onToggleFavorites: () -> Unit,
    selectedLanguageFilter: String?,
    onLanguageFilterChange: (String?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val languages = Language.getSupportedLanguages()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search translations...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Favorites filter
            FilterChip(
                onClick = onToggleFavorites,
                label = { Text("Favorites") },
                selected = showFavoritesOnly,
                leadingIcon = {
                    Icon(
                        imageVector = if (showFavoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorites filter",
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            // Clear filters button
            if (showFavoritesOnly || selectedLanguageFilter != null || searchQuery.isNotEmpty()) {
                AssistChip(
                    onClick = onClearFilters,
                    label = { Text("Clear") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear filters",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Language filter row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(languages) { language ->
                FilterChip(
                    onClick = {
                        onLanguageFilterChange(
                            if (selectedLanguageFilter == language.code) null else language.code
                        )
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = language.flag,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = language.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    selected = selectedLanguageFilter == language.code
                )
            }
        }
    }
}