// In ui/RecipeListViewModel.kt
package com.example.myrecipepal.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipepal.data.RecipeRepository
import com.example.myrecipepal.model.Meal
import kotlinx.coroutines.launch
import java.io.IOException

// This sealed interface represents the different states the UI can be in
sealed interface RecipeUiState {
    data class Success(val recipes: List<Meal>) : RecipeUiState
    object Error : RecipeUiState
    object Loading : RecipeUiState
}

class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)
        private set

    /**
     * Call getRecipes() on init so we can display status immediately.
     */
    init {
        // Load desserts by default when the ViewModel is first created
        getRecipes("Dessert")
    }

    /**
     * Gets recipe information from the API and updates the UI State.
     */
    fun getRecipes(category: String) {
        viewModelScope.launch {
            recipeUiState = RecipeUiState.Loading
            recipeUiState = try {
                val recipes = recipeRepository.getRecipesByCategory(category)
                RecipeUiState.Success(recipes)
            } catch (e: IOException) {
                RecipeUiState.Error
            }
        }
    }
}
