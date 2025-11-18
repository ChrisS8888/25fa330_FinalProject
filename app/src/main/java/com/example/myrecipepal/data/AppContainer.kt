// In data/AppContainer.kt
package com.example.myrecipepal.data

import com.example.myrecipepal.database.FavoritesRepository
import com.example.myrecipepal.database.RecipeDatabase
import com.example.myrecipepal.network.MealDbApi
import com.example.myrecipepal.network.MealDbApiService

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val recipeRepository: RecipeRepository
    val favoritesRepository: FavoritesRepository
}

/**
 * Implementation for the Dependency Injection container.
 */
class DefaultAppContainer(private val context: android.content.Context) : AppContainer {
    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/"

    // You can reuse the Retrofit object from your network package
    private val retrofitService: MealDbApiService by lazy {
        MealDbApi.retrofitService
    }

    // The repository is created here, using the Retrofit service
    override val recipeRepository: RecipeRepository by lazy {
        NetworkRecipeRepository(retrofitService)
    }

    /**
     * Provides the instance of the database repository.
     * It uses the application context to get the database instance.
     */
    override val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepository(RecipeDatabase.getDatabase(context).recipeDao())
    }
}
