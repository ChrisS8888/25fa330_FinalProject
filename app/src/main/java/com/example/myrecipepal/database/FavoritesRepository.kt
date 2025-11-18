package com.example.myrecipepal.database

import kotlinx.coroutines.flow.Flow

/**
 * Repository that wraps access to the Room favorites database.
 * This is separate from the network repository in the `data` package.
 */
class FavoritesRepository(
    private val recipeDao: RecipeDao
) {

    /** Stream of all favorites from the local DB. */
    fun getAllFavorites(): Flow<List<Recipe>> = recipeDao.getAllFavorites()

    /** Insert or update a favorite recipe. */
    suspend fun addFavorite(recipe: Recipe) {
        recipeDao.addFavorite(recipe)
    }

    /** Remove a recipe from favorites. */
    suspend fun removeFavorite(recipe: Recipe) {
        recipeDao.removeFavorite(recipe)
    }

    /** Check if a recipe is already in favorites. */
    suspend fun isFavorite(idMeal: String): Boolean {
        return recipeDao.isFavorite(idMeal)
    }

    /**
     * Toggle favorite status for a recipe.
     *
     * @return true if the recipe is now favorited, false if it was removed.
     */
    suspend fun toggleFavorite(recipe: Recipe): Boolean {
        return if (recipeDao.isFavorite(recipe.idMeal)) {
            recipeDao.removeFavorite(recipe)
            false
        } else {
            recipeDao.addFavorite(recipe)
            true
        }
    }
}
