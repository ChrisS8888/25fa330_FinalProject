package com.example.myrecipepal.ui

import com.example.myrecipepal.database.Recipe // Keep this import
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrecipepal.R
import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.ui.components.FavoriteRecipeCard // Import the new card

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onRecipeClicked: (String) -> Unit
) {
    val favoriteRecipes by favoritesViewModel.favorites.collectAsState()

    if (favoriteRecipes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_favorites_message))
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            items(items = favoriteRecipes, key = { recipe: Recipe -> recipe.idMeal }) { recipe: Recipe ->

                // This mapping is correct
                val meal = Meal(recipe)

                // --- ▼▼▼ THE FINAL FIX ▼▼▼ ---
                // Use the new FavoriteRecipeCard with its simple parameters
                FavoriteRecipeCard(
                    meal = meal,
                    onCardClick = { onRecipeClicked(meal.id) },
                    onRemoveFavorite = { favoritesViewModel.removeFavorite(recipe) }
                )
            }
        }
    }
}
