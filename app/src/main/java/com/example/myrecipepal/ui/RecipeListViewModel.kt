package com.example.myrecipepal.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipepal.data.RecipeRepository
import com.example.myrecipepal.database.FavoritesRepository
import com.example.myrecipepal.database.Recipe
import com.example.myrecipepal.model.Meal
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

// This sealed interface from your old code represents the different UI states. It's good, let's keep it.
sealed interface RecipeUiState {
    data class Success(val recipes: List<Meal>, val categoryName: String) : RecipeUiState
    object Error : RecipeUiState
    data class Loading(val categoryName: String) : RecipeUiState
}

class RecipeListViewModel(
    private val recipeRepository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // The mutable State that stores the UI status ---
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading(""))
        private set

    // State to hold the user's search text ---
    var searchQuery by mutableStateOf("")
        private set

    // Private property to hold the original, unfiltered list for searching ---
    private var originalRecipes: List<Meal> = emptyList()

    // This holds the IDs of all favorited recipes from the database ---
    val favoriteRecipeIds: StateFlow<Set<String>> =
        favoritesRepository.getAllFavorites()
            .map { favoriteRecipes -> favoriteRecipes.map { it.idMeal }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptySet()
            )

    init {
        getRecipes("Dessert") // Initial fetch
    }

    // The updated toggleFavorite function that writes to the database ---
    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            val recipe = Recipe(
                idMeal = meal.id,
                strMeal = meal.name,
                strMealThumb = meal.thumbnail,
                strInstructions = meal.instructions,
                strCategory = meal.category
            )
            // Use the correct repository to add/remove from the database
            if (favoriteRecipeIds.value.contains(recipe.idMeal)) {
                favoritesRepository.removeFavorite(recipe)
            } else {
                favoritesRepository.addFavorite(recipe)
            }
        }
    }

    // Function to fetch recipes from the API. This is what MainActivity should call.
    fun getRecipes(category: String) {
        viewModelScope.launch {
            // Avoids reloading if the data is already present and the category is the same.
            val currentState = recipeUiState
            if (currentState is RecipeUiState.Success && currentState.recipes.isNotEmpty() && currentState.categoryName == category) {
                return@launch
            }
            recipeUiState = RecipeUiState.Loading(category)
            try {
                // Fetch the recipes from the repository.
                val recipes = recipeRepository.getRecipesByCategory(category)
                // Store the original list for filtering.
                originalRecipes = recipes
                // Update the UI state with the full list.
                recipeUiState = RecipeUiState.Success(recipes, category)
            } catch (e: IOException) {
                recipeUiState = RecipeUiState.Error
            }
        }
    }

    // Function to handle search bar changes. It works with the RecipeUiState.
    fun onSearchQueryChanged(newQuery: String) {
        searchQuery = newQuery
        val currentState = recipeUiState
        if (currentState is RecipeUiState.Success) {
            val filteredList = if (newQuery.isBlank()) {
                originalRecipes // Show original list if search is empty
            } else {
                originalRecipes.filter { meal ->
                    meal.name.contains(newQuery, ignoreCase = true)
                }
            }
            // Update the UI state with the filtered list, keeping the same category name.
            recipeUiState = currentState.copy(recipes = filteredList)
        }
    }
}



