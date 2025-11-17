// In ui/RecipeListViewModel.kt
/*package com.example.myrecipepal.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.filter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipepal.data.RecipeRepository
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

    // --- ALL DETAIL SCREEN LOGIC HAS BEEN REMOVED FROM THIS FILE ---
    //          PUT IT IN THE DETAIL VIEW MODEL FILE


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
            recipeUiState = try {
                val recipes = recipeRepository.getRecipesByCategory(category)
                RecipeUiState.Success(recipes, category)
            } catch (e: IOException) {
                RecipeUiState.Error
            }
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
}


//// In ui/RecipeListViewModel.kt
//package com.example.myrecipepal.ui
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myrecipepal.data.RecipeRepository
//import com.example.myrecipepal.model.Meal
//import kotlinx.coroutines.launch
//import java.io.IOException
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//// This sealed interface represents the different states the UI can be in
///*sealed interface RecipeUiState {
//    data class Success(val recipes: List<Meal>) : RecipeUiState
//    object Error : RecipeUiState
//    object Loading : RecipeUiState
//}*/
//// Add the categoryName to your UiState to keep track of what's loaded
//sealed interface RecipeUiState {
//    data class Success(val recipes: List<Meal>, val categoryName: String) : RecipeUiState
//    object Error : RecipeUiState
//    data class Loading(val categoryName: String) : RecipeUiState
//}
////ADDED UI STATE FOR THE RECIPE DETAIL SCREEN
//sealed interface RecipeDetailUiState {
//    data class Success(val recipe: Meal) : RecipeDetailUiState
//    object Error : RecipeDetailUiState
//    object Loading : RecipeDetailUiState
//}
//
//class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
//    /** The mutable State that stores the status of the most recent request */
//    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading(""))
//        private set
//
//    // ADDED STATE FOR THE RECIPE DETAIL SCREEN
//    var recipeDetailUiState: RecipeDetailUiState by mutableStateOf(RecipeDetailUiState.Loading)
//        private set
//
//
//    // Private mutable state flow to hold the set of favorite recipe IDs
//    private val _favoriteRecipeIds = MutableStateFlow<Set<String>>(emptySet())
//
//    // Public immutable state flow for the UI to observe
//    val favoriteRecipeIds: StateFlow<Set<String>> = _favoriteRecipeIds.asStateFlow()
//
//    /**
//     * Call getRecipes() on init so we can display status immediately.
//     */
//    /*init {
//        // Load desserts by default when the ViewModel is first created
//        getRecipes("Dessert")
//    }*/
//
//    fun toggleFavorite(meal: Meal) {
//        viewModelScope.launch {
//            _favoriteRecipeIds.update { currentFavorites ->
//                if (currentFavorites.contains(meal.id)) {
//                    currentFavorites - meal.id
//                } else {
//                    currentFavorites + meal.id
//                }
//            }
//        }
//    }
//
//    // Gets full recipe details from the repository and updates the detail UI State.
//    fun getRecipeDetailsById(id: String) {
//        viewModelScope.launch {
//            recipeDetailUiState = RecipeDetailUiState.Loading
//            recipeDetailUiState = try {
//                val meal = recipeRepository.getRecipeDetailsById(id)
//                RecipeDetailUiState.Success(meal)
//            } catch (e: IOException) {
//                // Your repository correctly throws an exception, and you catch it here.
//                RecipeDetailUiState.Error
//            }
//        }
//    }
//
//    /**
//     * Gets recipe information from the API and updates the UI State.
//     */
//    fun getRecipes(category: String) {
//        // ... the rest of the function stays the same
//        viewModelScope.launch {
//            // Check if we are already showing the correct data to avoid reloading
//            val currentState = recipeUiState
//            if (currentState is RecipeUiState.Success && currentState.recipes.isNotEmpty() && currentState.categoryName == category) {
//                return@launch
//            }
//            // --- End of new check ---
//            recipeUiState = RecipeUiState.Loading(category) // Pass category to Loading state
//            recipeUiState = try {
//                val recipes = recipeRepository.getRecipesByCategory(category)
//                RecipeUiState.Success(recipes, category) // Pass category to Success state
//            } catch (e: IOException) {
//                RecipeUiState.Error
//            }
//        }
//    }
//}

