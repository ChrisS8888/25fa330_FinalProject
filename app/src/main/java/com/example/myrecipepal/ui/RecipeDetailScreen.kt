package com.example.myrecipepal.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myrecipepal.ui.components.FoodPatternBackground
import com.example.myrecipepal.ui.components.MyRecipePalTopAppBar

@Composable
fun RecipeDetailScreen(
    uiState: RecipeDetailUiState,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Wrap everything in the FoodPatternBackground
    FoodPatternBackground(
        modifier = modifier,
        alpha = 0.1f
    ) {
        Scaffold(
            topBar = {
                MyRecipePalTopAppBar(
                    title = "Recipe Details",
                    canNavigateBack = true,
                    navigateUp = navigateUp
                )
            },
            // 2. Make Scaffold transparent so the food shows through
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            when (uiState) {
                is RecipeDetailUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is RecipeDetailUiState.Success -> {
                    val recipe = uiState.recipe
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Recipe Image
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(recipe.thumbnail)
                                .crossfade(true)
                                .build(),
                            contentDescription = recipe.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                            error = painterResource(android.R.drawable.ic_menu_report_image)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Recipe Name - Force Black Color
                        Text(
                            text = recipe.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Recipe Category - Force Black Color
                        recipe.category?.let {
                            Text(
                                text = "Category: $it",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- Ingredients ---
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        recipe.getIngredientsWithMeasures().forEach { ingredient ->
                            Text(
                                text = "• $ingredient",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 4.dp),
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- Instructions ---
                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = recipe.instructions ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                    }
                }

                is RecipeDetailUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: Failed to load recipe.",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
