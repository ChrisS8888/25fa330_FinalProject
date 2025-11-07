// In MainActivity.kt
package com.example.myrecipepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrecipepal.ui.AppViewModelProvider
import com.example.myrecipepal.ui.CategoryResultsScreen
import com.example.myrecipepal.ui.RecipeListViewModel
import com.example.myrecipepal.ui.theme.MyRecipePalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyRecipePalTheme {
                // Get the ViewModel using the factory
                val recipeViewModel: RecipeListViewModel =
                    viewModel(factory = AppViewModelProvider.Factory)

                // Collect the set of favorite recipe IDs as state
                val favoriteIds by recipeViewModel.favoriteRecipeIds.collectAsState()

                // The CategoryResultsScreen now receives the state and the events it needs
                CategoryResultsScreen(
                    uiState = recipeViewModel.recipeUiState,
                    isFavorite = { meal -> favoriteIds.contains(meal.id) }, // NEW <---- SUPER COOL
                    onFavoriteClick = { meal -> recipeViewModel.toggleFavorite(meal) } // NEW <---- COOL
                )
            }
        }
    }
}
// OLD CODE BELOW
// JUST IN CASE YOU WANT TO COMPARE


//package com.example.myrecipepal
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.myrecipepal.ui.AppViewModelProvider
//import com.example.myrecipepal.ui.CategoryResultsScreen
//import com.example.myrecipepal.ui.RecipeListViewModel
//import com.example.myrecipepal.ui.RecipeUiState
//import com.example.myrecipepal.ui.theme.MyRecipePalTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyRecipePalTheme {
//                // Get the ViewModel using the factory you just created
//                val recipeViewModel: RecipeListViewModel =
//                    viewModel(factory = AppViewModelProvider.Factory)
//
//                // Call the temporary test screen
//                CategoryResultsScreen(uiState = recipeViewModel.recipeUiState)
//            }
//        }
//    }
//}

