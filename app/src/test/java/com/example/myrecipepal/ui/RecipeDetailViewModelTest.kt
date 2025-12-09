package com.example.myrecipepal.ui


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myrecipepal.data.RecipeRepository
import com.example.myrecipepal.model.Meal
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModelTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var recipeRepository: RecipeRepository
    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var testDispatcher: TestDispatcher


    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        recipeRepository = mockk()
        viewModel = RecipeDetailViewModel(recipeRepository)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getRecipeDetails_success_UI_state_is_updated_to_Success`() = runTest {
        val mealId = "52772"
        val fakeRecipe = Meal(id = mealId, name = "Spicy pasta", instructions = "Cook pasta...") //used fake data
        coEvery { recipeRepository.getRecipeDetailsById(mealId) } returns fakeRecipe
        viewModel.getRecipeDetails(mealId)
        val expectedState = RecipeDetailUiState.Success(fakeRecipe)
        assertEquals(expectedState, viewModel.recipeDetailUiState)
    }


    @Test
    fun `getRecipeDetails_network_error_UI_state_is_updated_to_Error`() = runTest {
        val mealId = "invalidId"
        coEvery { recipeRepository.getRecipeDetailsById(mealId) } throws IOException()
        viewModel.getRecipeDetails(mealId)
        assertEquals(RecipeDetailUiState.Error, viewModel.recipeDetailUiState)
    }
}
