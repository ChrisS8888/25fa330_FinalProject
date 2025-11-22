/*
package com.example.myrecipepal.ui

import com.example.myrecipepal.database.Recipe
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrecipepal.R
import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.ui.components.FavoriteRecipeCard
import com.example.myrecipepal.ui.components.FoodPatternBackground

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onRecipeClicked: (String) -> Unit,
    modifier: Modifier = Modifier // Add modifier parameter for good practice
) {
    val favoriteRecipes by favoritesViewModel.favorites.collectAsState()

    // 1. Wrap everything in the FoodPatternBackground
    FoodPatternBackground(modifier = modifier) {
        // 2. Use a transparent Scaffold or Box to hold the content
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->

            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
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
                        items(
                            items = favoriteRecipes,
                            key = { recipe: Recipe -> recipe.idMeal }
                        ) { recipe: Recipe ->
                            val meal = Meal(recipe)

                            FavoriteRecipeCard(
                                meal = meal,
                                onCardClick = { onRecipeClicked(meal.id) },
                                onRemoveFavorite = { favoritesViewModel.removeFavorite(recipe) }
                            )
                        }
                    }
                }
            }
        }
    }
}*/

package com.example.myrecipepal.ui

import com.example.myrecipepal.database.Recipe
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrecipepal.R
import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.ui.components.FavoriteRecipeCard
import com.example.myrecipepal.ui.components.FoodPatternBackground

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onRecipeClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Collect the search query and the FILTERED list
    val searchQuery by favoritesViewModel.searchQuery.collectAsState()
    val favoriteRecipes by favoritesViewModel.filteredFavorites.collectAsState()

    // 2. Wrap everything in the FoodPatternBackground
    FoodPatternBackground(modifier = modifier) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // 3. Add the Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { favoritesViewModel.updateSearchQuery(it) },
                    label = {
                        Text(
                            text = "Search favorites...",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )

                // 4. Display the list or "Empty" message
                Box(modifier = Modifier.fillMaxSize()) {
                    if (favoriteRecipes.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Show different message depending on if searching or just empty
                            val message = if (searchQuery.isNotEmpty()) {
                                "No favorites found for '$searchQuery'"
                            } else {
                                stringResource(R.string.no_favorites_message)
                            }

                            Text(
                                text = message,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            items(
                                items = favoriteRecipes,
                                key = { recipe: Recipe -> recipe.idMeal }
                            ) { recipe: Recipe ->
                                val meal = Meal(recipe)

                                FavoriteRecipeCard(
                                    meal = meal,
                                    onCardClick = { onRecipeClicked(meal.id) },
                                    onRemoveFavorite = { favoritesViewModel.removeFavorite(recipe) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
