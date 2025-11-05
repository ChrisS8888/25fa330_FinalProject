package com.example.myrecipepal.data

import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.network.MealDbApiService

/**
 * Repository that fetches a list of recipes from TheMealDB API.
 */
interface RecipeRepository {
    suspend fun getRecipesByCategory(category: String): List<Meal>
}

/**
 * Network implementation of the Repository.
 */
class NetworkRecipeRepository(
    private val mealDbApiService: MealDbApiService
) : RecipeRepository {
    override suspend fun getRecipesByCategory(category: String): List<Meal> {
        // The API returns a MealApiResponse, we only want the list of meals
        return mealDbApiService.getRecipesByCategory(category).meals
    }
}
