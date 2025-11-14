// In ui/RecipeDetailViewModel.kt
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

/**
 * Represents the UI state for the Recipe Detail screen. This is the single source of truth.
 */
sealed interface RecipeDetailUiState {
    data class Success(val recipe: Meal) : RecipeDetailUiState
    object Error : RecipeDetailUiState
    object Loading : RecipeDetailUiState
}

/**
 * ViewModel responsible for the business logic and state of the RecipeDetailScreen.
 */
class RecipeDetailViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    /** The mutable State that stores the status of the recipe detail request. */
    var recipeDetailUiState: RecipeDetailUiState by mutableStateOf(RecipeDetailUiState.Loading)
        private set

    /**
     * Gets the full recipe details from the repository and updates the UI state.
     */
    fun getRecipeDetails(id: String) {
        viewModelScope.launch {
            recipeDetailUiState = RecipeDetailUiState.Loading
            recipeDetailUiState = try {
                val recipe = recipeRepository.getRecipeDetailsById(id)
                RecipeDetailUiState.Success(recipe)
            } catch (e: IOException) {
                // The repository throws an exception on failure, which we catch here.
                RecipeDetailUiState.Error
            }
        }
    }
}


