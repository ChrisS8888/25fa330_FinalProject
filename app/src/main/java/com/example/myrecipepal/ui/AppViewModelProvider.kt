// In ui/AppViewModelProvider.kt
package com.example.myrecipepal.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myrecipepal.MyRecipePalApplication

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for RecipeListViewModel
        initializer {
            // Get the repository from the application's dependency container
            val recipeRepository = myRecipePalApplication().container.recipeRepository
            // Create the ViewModel, passing the repository
            RecipeListViewModel(recipeRepository = recipeRepository)
        }

        initializer {
            val recipeRepository = myRecipePalApplication().container.recipeRepository
            RecipeDetailViewModel(recipeRepository = recipeRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MyRecipePalApplication].
 */
fun CreationExtras.myRecipePalApplication(): MyRecipePalApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyRecipePalApplication)
