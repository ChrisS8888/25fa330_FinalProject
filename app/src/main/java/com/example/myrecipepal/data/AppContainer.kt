// In data/AppContainer.kt
package com.example.myrecipepal.data

import com.example.myrecipepal.network.MealDbApi
import com.example.myrecipepal.network.MealDbApiService

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val recipeRepository: RecipeRepository
}

/**
 * Implementation for the Dependency Injection container.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/"

    // You can reuse the Retrofit object from your network package
    private val retrofitService: MealDbApiService by lazy {
        MealDbApi.retrofitService
    }

    // The repository is created here, using the Retrofit service
    override val recipeRepository: RecipeRepository by lazy {
        NetworkRecipeRepository(retrofitService)
    }
}
