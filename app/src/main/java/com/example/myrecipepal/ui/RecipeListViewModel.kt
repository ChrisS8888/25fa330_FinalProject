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
/*sealed interface RecipeUiState {
    data class Success(val recipes: List<Meal>) : RecipeUiState
    object Error : RecipeUiState
    object Loading : RecipeUiState
}*/
// Add the categoryName to your UiState to keep track of what's loaded
sealed interface RecipeUiState {
    data class Success(val recipes: List<Meal>, val categoryName: String) : RecipeUiState
    object Error : RecipeUiState
    data class Loading(val categoryName: String) : RecipeUiState
}

class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading(""))
        private set

    /**
     * Call getRecipes() on init so we can display status immediately.
     */
    /*init {
        // Load desserts by default when the ViewModel is first created
        getRecipes("Dessert")
    }*/

    /**
     * Gets recipe information from the API and updates the UI State.
     */
    fun getRecipes(category: String) {
        // ... the rest of the function stays the same
        viewModelScope.launch {
            // Check if we are already showing the correct data to avoid reloading
            val currentState = recipeUiState
            if (currentState is RecipeUiState.Success && currentState.recipes.isNotEmpty() && currentState.categoryName == category) {
                return@launch
            }
            // --- End of new check ---
            recipeUiState = RecipeUiState.Loading(category) // Pass category to Loading state
            recipeUiState = try {
                val recipes = recipeRepository.getRecipesByCategory(category)
                RecipeUiState.Success(recipes, category) // Pass category to Success state
            } catch (e: IOException) {
                RecipeUiState.Error
            }
        }
    }
}

