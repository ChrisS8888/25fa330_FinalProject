/*package com.example.myrecipepal.data

import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.network.MealDbApiService

/**
 * Repository that fetches a list of recipes from TheMealDB API.
 */
interface RecipeRepository {
    suspend fun getRecipesByCategory(category: String): List<Meal>
    suspend fun getRecipeDetailsById(id: String): Meal
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

    override suspend fun getRecipeDetailsById(id: String): Meal {
        // The API returns a list containing just one meal, so we safely get the first element.
        return mealDbApiService.getRecipeDetailsById(id).meals.first()
    }
}*/

// In data/RecipeRepository.kt
package com.example.myrecipepal.data

import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.network.MealDbApiService
import java.io.IOException

/**
 * Repository that fetches a list of recipes from TheMealDB API.
 */
interface RecipeRepository {
    suspend fun getRecipesByCategory(category: String): List<Meal>
    suspend fun getRecipeDetailsById(id: String): Meal
}

/**
 * Network implementation of the Repository.
 */
class NetworkRecipeRepository(
    private val mealDbApiService: MealDbApiService
) : RecipeRepository {
    override suspend fun getRecipesByCategory(category: String): List<Meal> {
        // ▼▼▼ FIX 1 ▼▼▼
        // If the meals list from the API is null, return an empty list instead.
        val meals = mealDbApiService.getRecipesByCategory(category).meals ?: emptyList()

        // Filter out any meals that are missing a name or a thumbnail image.
        return meals.filter { meal ->
            !meal.name.isNullOrBlank() && !meal.thumbnail.isNullOrBlank()
        }
    }

    override suspend fun getRecipeDetailsById(id: String): Meal {
        // ▼▼▼ FIX 2 ▼▼▼
        // Safely get the first meal from the list. If the list is null or empty,
        // throw an exception that the ViewModel can catch.
        val meal = mealDbApiService.getRecipeDetailsById(id).meals?.firstOrNull()
            ?: throw IOException("Recipe with ID $id not found or data is null.")

        // Validate that the received meal has the necessary details.
        // If not, throw an exception.
        if (meal.instructions.isNullOrBlank() || meal.thumbnail.isNullOrBlank()) {
            throw IOException("Incomplete recipe data for ID $id.")
        }

        return meal
    }
}
