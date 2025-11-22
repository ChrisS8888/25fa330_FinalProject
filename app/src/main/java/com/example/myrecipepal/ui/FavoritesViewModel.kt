package com.example.myrecipepal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipepal.database.Recipe
import com.example.myrecipepal.database.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorites screen.
 * Talks to the Room database through FavoritesRepository.
 */
class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // 1. State for the search text (User types here)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // 2. Get the raw list from the database (Internal use only)
    private val _allFavorites = favoritesRepository.getAllFavorites()

    // 3. Expose the FILTERED list to the UI
    // This combines the user's text + the database list
    val filteredFavorites: StateFlow<List<Recipe>> = combine(_searchQuery, _allFavorites) { query, list ->
        if (query.isBlank()) {
            list // Return all if search is empty
        } else {
            list.filter { recipe ->
                recipe.strMeal.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    /** Update the search text when user types */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /** Add a recipe to favorites. */
    fun addFavorite(recipe: Recipe) {
        viewModelScope.launch {
            favoritesRepository.addFavorite(recipe)
        }
    }

    /** Remove a recipe from favorites. */
    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(recipe)
        }
    }

    /**
     * Toggle helper for a heart icon.
     * onResult(true)  → now favorited
     * onResult(false) → removed from favorites
     */
    fun toggleFavorite(recipe: Recipe, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val nowFavorite = favoritesRepository.toggleFavorite(recipe)
            onResult(nowFavorite)
        }
    }
}
