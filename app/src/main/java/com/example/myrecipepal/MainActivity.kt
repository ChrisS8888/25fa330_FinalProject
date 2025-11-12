/*package com.example.myrecipepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrecipepal.ui.AppViewModelProvider
import com.example.myrecipepal.ui.CategoryResultsScreen
import com.example.myrecipepal.ui.RecipeListViewModel
import com.example.myrecipepal.ui.RecipeUiState
import com.example.myrecipepal.ui.theme.MyRecipePalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyRecipePalTheme {
                // Get the ViewModel using the factory you just created
                val recipeViewModel: RecipeListViewModel =
                    viewModel(factory = AppViewModelProvider.Factory)

                // Call the temporary test screen
                CategoryResultsScreen(uiState = recipeViewModel.recipeUiState)
            }
        }
    }
}*/

// In MainActivity.kt
package com.example.myrecipepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myrecipepal.ui.AppViewModelProvider
import com.example.myrecipepal.ui.CategoryResultsScreen
import com.example.myrecipepal.ui.HomeScreen
import com.example.myrecipepal.ui.RecipeListViewModel
import com.example.myrecipepal.ui.theme.MyRecipePalTheme
import com.example.myrecipepal.ui.RecipeDetailScreen // You will create this file next
import com.example.myrecipepal.ui.RecipeDetailViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyRecipePalTheme {
                Surface {
                    // 1. Create a NavController
                    val navController = rememberNavController()
                    val recipeViewModel: RecipeListViewModel =
                        viewModel(factory = AppViewModelProvider.Factory)
                    // Collect the set of favorite recipe IDs as state
                    val favoriteIds by recipeViewModel.favoriteRecipeIds.collectAsState()

                    // 2. Create a NavHost to define the navigation graph
                    NavHost(
                        navController = navController,
                        startDestination = "home" // The app now starts at the home screen
                    ) {
                        // Route for the Home Screen
                        composable(route = "home") {
                            HomeScreen(
                                onCategorySelected = { category ->
                                    // Navigate to the results screen, passing the category name in the route
                                    navController.navigate("results/$category")
                                }
                            )
                        }

                        // Route for the Category Results Screen
                        /*composable(
                            route = "results/{category}", // This route accepts a "category" argument
                            arguments = listOf(navArgument("category") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Retrieve the category name from the navigation arguments
                            val category = backStackEntry.arguments?.getString("category") ?: "Dessert"
                            // Tell the ViewModel to load the recipes for this category
                            recipeViewModel.getRecipes(category)
                            // Display the results screen
                            CategoryResultsScreen(uiState = recipeViewModel.recipeUiState)
                        }*/

                        composable(
                            route = "results/{category}",
                            arguments = listOf(navArgument("category") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val category =
                                backStackEntry.arguments?.getString("category") ?: "Dessert"
                            val recipeListViewModel: RecipeListViewModel =
                                viewModel(factory = AppViewModelProvider.Factory)
                            recipeListViewModel.getRecipes(category)

                            CategoryResultsScreen(
                                uiState = recipeListViewModel.recipeUiState,
                                isFavorite = { meal -> favoriteIds.contains(meal.id) }, // NEW <---- SUPER COOL
                                onFavoriteClick = { meal -> recipeViewModel.toggleFavorite(meal) },// NEW <---- COOL
                                // ADD ONCLICK HANDLER
                                onRecipeClicked = { mealId ->
                                    navController.navigate("details/$mealId")
                                }
                            )
                        }

                        composable(
                            route = "details/{mealId}",
                            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val mealId = backStackEntry.arguments?.getString("mealId")
                            val detailViewModel: RecipeDetailViewModel =
                                viewModel(factory = AppViewModelProvider.Factory)

                            // Make sure we have a mealId before fetching
                            LaunchedEffect(mealId) {
                                if (mealId != null) {
                                    detailViewModel.getRecipeDetails(mealId)
                                }
                            }

                            RecipeDetailScreen(uiState = detailViewModel.recipeDetailUiState)
                        }
                    }
                }
            }
        }
    }
}


