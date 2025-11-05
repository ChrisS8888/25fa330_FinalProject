package com.example.myrecipepal

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
                SimpleRecipeTestScreen(uiState = recipeViewModel.recipeUiState)
            }
        }
    }
}

@Composable
fun SimpleRecipeTestScreen(uiState: RecipeUiState) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is RecipeUiState.Loading -> {
                Text("Loading recipes...")
            }
            is RecipeUiState.Success -> {
                // If it succeeds, show the number of recipes found
                Text("Success! Found ${uiState.recipes.size} recipes.")
            }
            is RecipeUiState.Error -> {
                Text("Error: Failed to load recipes.")
            }
        }
    }
}

