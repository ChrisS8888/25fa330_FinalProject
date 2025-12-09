package com.example.myrecipepal.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumentation tests for the favorites Room database in MyRecipePal.
 *
 * These tests verify:
 *  1. addFavorite() + getAllFavorites() + isFavorite()
 *  2. removeFavorite() + isFavorite() behavior
 *
 * An in-memory Room database is used, so no real app data is touched.
 */
@RunWith(AndroidJUnit4::class)
class RecipeDatabaseTest {


    private lateinit var db: RecipeDatabase
    private lateinit var dao: RecipeDao
    private lateinit var context: Context


    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()


        // In-memory DB for testing – discarded after tests finish.
        db = Room.inMemoryDatabaseBuilder(
            context,
            RecipeDatabase::class.java
        )
            .allowMainThreadQueries() // OK for tests
            .build()


        dao = db.recipeDao()
    }


    @After
    fun tearDown() {
        db.close()
    }


    /**
     * TEST 1:
     *  Insert a favorite recipe and verify:
     *   - It shows up in getAllFavorites()
     *   - isFavorite(id) returns true
     */
    @Test
    fun addFavorite_andReadBackAndCheckIsFavorite() = runBlocking {
        val recipe = Recipe(
            idMeal = "12345",
            strMeal = "Test Pasta",
            strMealThumb = "https://example.com/img.jpg",
            strInstructions = "Boil water. Cook pasta.",
            strCategory = "Test Category"
        )


        // Insert favorite
        dao.addFavorite(recipe)


        // Read favorites list from Flow
        val favorites = dao.getAllFavorites().first()
        assertTrue("Favorites list should not be empty after addFavorite()", favorites.isNotEmpty())


        val saved = favorites.first()
        assertEquals("12345", saved.idMeal)
        assertEquals("Test Pasta", saved.strMeal)


        // Check isFavorite() for this id
        val isFav = dao.isFavorite("12345")
        assertTrue("isFavorite() should return true for inserted recipe", isFav)
    }


    /**
     * TEST 2:
     *  Insert a favorite recipe, remove it, and verify:
     *   - It doesn't show in getAllFavorites()
     *   - isFavorite(id) returns false
     */
    @Test
    fun removeFavorite_makesListEmptyAndIsFavoriteFalse() = runBlocking {
        val recipe = Recipe(
            idMeal = "999",
            strMeal = "Delete Test",
            strMealThumb = null,
            strInstructions = "Testing removal of favorite",
            strCategory = "Test Category"
        )


        // Insert favorite first
        dao.addFavorite(recipe)


        // Remove it
        dao.removeFavorite(recipe)


        // Favorites list should now be empty
        val favoritesAfterRemoval = dao.getAllFavorites().first()
        assertTrue(
            "Favorites list should be empty after removeFavorite()",
            favoritesAfterRemoval.isEmpty()
        )


        // isFavorite() should return false for this id
        val isFavAfterRemoval = dao.isFavorite("999")
        assertFalse(
            "isFavorite() should be false after the recipe is removed",
            isFavAfterRemoval
        )
    }
}
