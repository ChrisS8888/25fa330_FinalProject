// In ui/CategoryResultsScreen.kt
package com.example.myrecipepal.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.ui.components.RecipeCard

@Composable
fun CategoryResultsScreen(uiState: RecipeUiState, modifier: Modifier = Modifier) {
    when (uiState) {
        is RecipeUiState.Loading -> {
            // You can make a fancier loading indicator later
            Text("Loading recipes...", modifier = modifier.fillMaxSize())
        }
        is RecipeUiState.Success -> {
            RecipeGrid(recipes = uiState.recipes, modifier = modifier)
        }
        is RecipeUiState.Error -> {
            Text("Error: Failed to load recipes.", modifier = modifier.fillMaxSize())
        }
    }
}

@Composable
fun RecipeGrid(recipes: List<Meal>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        items(items = recipes, key = { recipe -> recipe.id }) { recipe ->
            RecipeCard(meal = recipe)
        }
    }
}
