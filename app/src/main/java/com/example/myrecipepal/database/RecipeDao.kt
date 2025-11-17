package com.example.myrecipepal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY strMeal COLLATE NOCASE ASC")
    fun getAllFavorites(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(recipe: Recipe)

    @Delete
    suspend fun removeFavorite(recipe: Recipe)

    // 🔹 THIS is the function your repository needs
    @Query("SELECT EXISTS(SELECT 1 FROM recipes WHERE idMeal = :id LIMIT 1)")
    suspend fun isFavorite(id: String): Boolean
}
