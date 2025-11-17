package com.example.myrecipepal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipepal.database.Recipe
import com.example.myrecipepal.database.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorites screen.
 * Talks to the Room database through RecipeRepository.
 */
class FavoritesViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    // Stream of favorites for the UI to observe.
    val favorites: StateFlow<List<Recipe>> =
        repository.getAllFavorites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /** Add a recipe to favorites. */
    fun addFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.addFavorite(recipe)
        }
    }

    /** Remove a recipe from favorites. */
    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.removeFavorite(recipe)
        }
    }

    /**
     * Toggle helper for a heart icon.
     * onResult(true)  → now favorited
     * onResult(false) → removed from favorites
     */
    fun toggleFavorite(recipe: Recipe, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val nowFavorite = repository.toggleFavorite(recipe)
            onResult(nowFavorite)
        }
    }
}
