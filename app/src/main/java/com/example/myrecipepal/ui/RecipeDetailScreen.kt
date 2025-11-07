package com.example.myrecipepal.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// In ui/RecipeDetailScreen.kt
@Composable
fun RecipeDetailScreen(uiState: RecipeDetailUiState) {
    // This is a temporary screen. We will build the real one later.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            is RecipeDetailUiState.Loading -> Text("Loading details...")
            is RecipeDetailUiState.Success -> Text("Details for: ${uiState.recipe.name}")
            is RecipeDetailUiState.Error -> Text("Error loading details.")
        }
    }
}