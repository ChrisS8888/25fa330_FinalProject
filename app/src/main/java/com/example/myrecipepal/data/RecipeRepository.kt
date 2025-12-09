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
        // If the meals list from the API is null, return an empty list instead.
        val meals = mealDbApiService.getRecipesByCategory(category).meals ?: emptyList()

        // Filter out any meals that are missing a name or a thumbnail image.
        return meals.filter { meal ->
            !meal.name.isNullOrBlank() && !meal.thumbnail.isNullOrBlank()
        }
    }

    override suspend fun getRecipeDetailsById(id: String): Meal {
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
