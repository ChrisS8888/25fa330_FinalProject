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
 * Represents the UI state for the Recipe Detail screen.
 */
sealed interface RecipeDetailUiState {
    data class Success(val recipe: Meal) : RecipeDetailUiState
    object Error : RecipeDetailUiState
    object Loading : RecipeDetailUiState
}

class RecipeDetailViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    var recipeDetailUiState: RecipeDetailUiState by mutableStateOf(RecipeDetailUiState.Loading)
        private set

    fun getRecipeDetails(id: String) {
        viewModelScope.launch {
            recipeDetailUiState = RecipeDetailUiState.Loading
            recipeDetailUiState = try {
                // The API returns a list with one item, so we get the first.
                val recipe = recipeRepository.getRecipeDetailsById(id)//[0]
                RecipeDetailUiState.Success(recipe)
            } catch (e: IOException) {
                RecipeDetailUiState.Error
            }
        }
    }
}
