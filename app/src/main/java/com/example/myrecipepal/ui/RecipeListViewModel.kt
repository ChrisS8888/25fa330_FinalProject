// In ui/RecipeListViewModel.kt
/*package com.example.myrecipepal.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipepal.data.RecipeRepository
import com.example.myrecipepal.database.Recipe
import com.example.myrecipepal.model.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * This ViewModel is now ONLY responsible for the list of recipes and favorites.
 */

// This sealed interface represents the different states the list screen can be in.
sealed interface RecipeUiState {
    data class Success(val recipes: List<Meal>, val categoryName: String) : RecipeUiState
    object Error : RecipeUiState
    data class Loading(val categoryName: String) : RecipeUiState
}

class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    /** The mutable State that stores the status of the recipe list request */
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading(""))
        private set

    // --- NEW: State to hold the user's search text ---
    var searchQuery by mutableStateOf("")
        private set

    // --- NEW: Private property to hold the original, unfiltered list ---
    private var originalRecipes: List<Meal> = emptyList()

    // Private mutable state flow to hold the set of favorite recipe IDs
    private val _favoriteRecipeIds = MutableStateFlow<Set<String>>(emptySet())

    // Public immutable state flow for the UI to observe
    val favoriteRecipeIds: StateFlow<Set<String>> = _favoriteRecipeIds.asStateFlow()

    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            _favoriteRecipeIds.update { currentFavorites ->
                if (currentFavorites.contains(meal.id)) {
                    currentFavorites - meal.id
                } else {
                    currentFavorites + meal.id
                }
            }
        }
    }

    /*fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            val recipeId = meal.id
            if (favoriteRecipeIds.value.contains(recipeId)) {
                // If it's already a favorite, create a recipe object to remove it
                val recipeToRemove = Recipe(
                    idMeal = meal.id,
                    strMeal = meal.name,
                    strMealThumb = meal.thumbnail,
                    // --- Pass the new fields for removal ---
                    strInstructions = meal.instructions,
                    strCategory = meal.category
                )
                recipeRepository.removeFavorite(recipeToRemove)
            } else {
                // If it's a new favorite, create a recipe object to add it
                val recipeToAdd = Recipe(
                    idMeal = meal.id,
                    strMeal = meal.name,
                    strMealThumb = meal.thumbnail,
                    // --- Pass the new fields for addition ---
                    strInstructions = meal.instructions,
                    strCategory = meal.category
                )
                recipeRepository.addFavorite(recipeToAdd)
            }
        }
    }*/

    /**
     * Gets recipe information from the API and updates the UI State for the list screen.
     */
    fun getRecipes(category: String) {
        viewModelScope.launch {
            val currentState = recipeUiState
            // Avoids reloading if the data is already present.
            if (currentState is RecipeUiState.Success && currentState.recipes.isNotEmpty() && currentState.categoryName == category) {
                return@launch
            }
            recipeUiState = RecipeUiState.Loading(category)
            try {
                // Fetch the recipes from the repository.
                val recipes = recipeRepository.getRecipesByCategory(category)
                // --- NEW: Store the original list ---
                originalRecipes = recipes

                // Initially, the UI state shows the full, unfiltered list.
                recipeUiState = RecipeUiState.Success(recipes, category)
            } catch (e: IOException) {
                recipeUiState = RecipeUiState.Error
            }
        }
    }

    // --- NEW: Function to handle changes in the search bar text ---
    fun onSearchQueryChanged(newQuery: String) {
        // Update the search query state that the UI observes.
        searchQuery = newQuery

        // Get the current success state, if it exists.
        val currentState = recipeUiState
        if (currentState is RecipeUiState.Success) {
            // Filter the original list based on the new query.
            val filteredList = if (newQuery.isBlank()) {
                // If the search bar is empty, show the original full list.
                originalRecipes
            } else {
                // Otherwise, filter the original list by meal name.
                originalRecipes.filter { meal ->
                    meal.name.contains(newQuery, ignoreCase = true)
                }
            }
            // Update the UI state with the new filtered list.
            recipeUiState = currentState.copy(recipes = filteredList)
        }
    }
}*/

// In ui/RecipeListViewModel.kt
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

// FIX: The constructor accepts BOTH repositories
class RecipeListViewModel(
    private val recipeRepository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    // --- FROM OLD CODE: The mutable State that stores the UI status ---
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading(""))
        private set

    // --- FROM OLD CODE: State to hold the user's search text ---
    var searchQuery by mutableStateOf("")
        private set

    // --- FROM OLD CODE: Private property to hold the original, unfiltered list for searching ---
    private var originalRecipes: List<Meal> = emptyList()

    // --- FROM NEW CODE: This holds the IDs of all favorited recipes from the database ---
    val favoriteRecipeIds: StateFlow<Set<String>> =
        favoritesRepository.getAllFavorites()
            .map { favoriteRecipes -> favoriteRecipes.map { it.idMeal }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptySet()
            )

    init {
        // We will now use your original getRecipes function.
        getRecipes("Dessert") // Initial fetch
    }

    // --- FROM NEW CODE: The updated toggleFavorite function that writes to the database ---
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

    // --- FROM OLD CODE: Your original function to fetch recipes from the API. This is what MainActivity should call. ---
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

    // --- FROM OLD CODE: Your function to handle search bar changes. It works with the RecipeUiState. ---
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



