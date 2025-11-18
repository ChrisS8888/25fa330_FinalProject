// In ui/CategoryResultsScreen.kt
package com.example.myrecipepal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myrecipepal.R
import com.example.myrecipepal.model.Meal
import com.example.myrecipepal.ui.components.RecipeCard
import androidx.compose.material3.Scaffold
import com.example.myrecipepal.ui.components.MyRecipePalTopAppBar

@Composable
fun CategoryResultsScreen(
    uiState: RecipeUiState,
    searchQuery: String,                      // For displaying the text in the search bar
    onSearchQueryChanged: (String) -> Unit,   // For notifying the ViewModel when text changes
    onRecipeClicked: (String) -> Unit,        // For navigating to the detail screen
    isFavorite: (Meal) -> Boolean,            // For checking if a recipe is a favorite
    onFavoriteClick: (Meal) -> Unit,          // For handling a favorite button click
    navigateUp: () -> Unit,                    // For navigating back
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MyRecipePalTopAppBar(
                title = "Recipes",
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        // Use a Column to place the search bar above the recipe grid.
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            // The search bar UI. It's connected to the ViewModel via the parameters.
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text(stringResource(R.string.filter_recipes_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            // This `when` block handles showing the correct UI for the current state.
            when (uiState) {
                is RecipeUiState.Loading -> {
                    // Use the category name from the Loading state.
                    Text(
                        "Loading recipes for ${uiState.categoryName}...",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is RecipeUiState.Success -> {
                    // Check if the list is empty because of a filter.
                    if (uiState.recipes.isEmpty() && searchQuery.isNotEmpty()) {
                        Text(
                            "No recipes found for '$searchQuery'",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        // If we have recipes, display them in the grid.
                        RecipeGrid(
                            recipes = uiState.recipes, // This list is now filtered by the ViewModel
                            onRecipeClicked = onRecipeClicked,
                            isFavorite = isFavorite,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }

                is RecipeUiState.Error -> {
                    Text("Error: Failed to load recipes.", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }

    @Composable
    fun RecipeGrid(
        recipes: List<Meal>,
        onRecipeClicked: (String) -> Unit,
        isFavorite: (Meal) -> Boolean,
        onFavoriteClick: (Meal) -> Unit,
        modifier: Modifier = Modifier
    ) {
        LazyVerticalGrid(
            // Using Adaptive makes it responsive to different screen sizes.
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = modifier.padding(horizontal = 8.dp)
        ) {
            items(items = recipes, key = { recipe -> recipe.id }) { recipe ->
                RecipeCard(
                    meal = recipe,
                    isFavorite = isFavorite(recipe),
                    onClick = { onRecipeClicked(recipe.id) },
                    onFavoriteClick = { onFavoriteClick(recipe) }
                )
            }
        }
    }
}

