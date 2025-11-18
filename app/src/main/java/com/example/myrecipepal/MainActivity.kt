// In MainActivity.kt
package com.example.myrecipepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myrecipepal.ui.AppViewModelProvider
import com.example.myrecipepal.ui.CategoryResultsScreen
import com.example.myrecipepal.ui.FavoritesScreen
import com.example.myrecipepal.ui.HomeScreen
import com.example.myrecipepal.ui.RecipeDetailScreen
import com.example.myrecipepal.ui.RecipeDetailViewModel
import com.example.myrecipepal.ui.RecipeListViewModel
import com.example.myrecipepal.ui.components.BottomNavigationBar
import com.example.myrecipepal.ui.theme.MyRecipePalTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyRecipePalTheme {
                Surface {
                    val navController = rememberNavController()
                    // Scaffold provides the structure for the bottom nav bar
                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController) }
                    ) { innerPadding ->
                        // The NavHost is now the main content of the Scaffold
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val recipeListViewModel: RecipeListViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val favoriteIds by recipeListViewModel.favoriteRecipeIds.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier // Apply padding from Scaffold
    ) {
        // --- Home Route (Entry point for the Home flow) ---
        composable(route = "home") {
            HomeScreen(onCategorySelected = { category ->
                navController.navigate("results/$category")
            })
        }

        // --- Favorites Route ---
        composable(route = "favorites") {
            FavoritesScreen(onRecipeClicked = { mealId ->
                navController.navigate("details/$mealId")
            })
        }

        // --- Category Results Route ---
        composable(
            route = "results/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Dessert"
            LaunchedEffect(category) { recipeListViewModel.getRecipes(category) }

            CategoryResultsScreen(
                uiState = recipeListViewModel.recipeUiState,
                searchQuery = recipeListViewModel.searchQuery,
                onSearchQueryChanged = { recipeListViewModel.onSearchQueryChanged(it) },
                isFavorite = { meal -> favoriteIds.contains(meal.id) },
                onFavoriteClick = { meal -> recipeListViewModel.toggleFavorite(meal) },
                onRecipeClicked = { mealId -> navController.navigate("details/$mealId") },
                navigateUp = { navController.navigateUp() }
            )
        }

        // --- Recipe Detail Route ---
        composable(
            route = "details/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            val detailViewModel: RecipeDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LaunchedEffect(mealId) {
                if (mealId != null) detailViewModel.getRecipeDetails(mealId)
            }
            RecipeDetailScreen(
                uiState = detailViewModel.recipeDetailUiState,
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}



