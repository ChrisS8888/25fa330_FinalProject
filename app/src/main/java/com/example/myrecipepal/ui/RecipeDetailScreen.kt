//package com.example.myrecipepal.ui
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//
//// In ui/RecipeDetailScreen.kt
//@Composable
//fun RecipeDetailScreen(uiState: RecipeDetailUiState) {
//    // This is a temporary screen. We will build the real one later.
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        when (uiState) {
//            is RecipeDetailUiState.Loading -> Text("Loading details...")
//            is RecipeDetailUiState.Success -> Text("Details for: ${uiState.recipe.name}")
//            is RecipeDetailUiState.Error -> Text("Error loading details.")
//        }
//    }
//}
// In ui/RecipeDetailScreen.kt
package com.example.myrecipepal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myrecipepal.R

@Composable
fun RecipeDetailScreen(uiState: RecipeDetailUiState) {
    when (uiState) {
        is RecipeDetailUiState.Loading -> {
            // Show a loading spinner in the center of the screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is RecipeDetailUiState.Success -> {
            // Once data is loaded successfully, display the recipe details
            val recipe = uiState.recipe
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Makes the content scrollable
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start // Align text to the start
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
                    // Using built-in Android drawables for placeholders
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_menu_report_image)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recipe Name
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Recipe Category (if available)
                recipe.category?.let {
                    Text(
                        text = "Category: $it",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Instructions Section
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    // Your repository ensures instructions are not null/blank here
                    text = recipe.instructions!!,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        is RecipeDetailUiState.Error -> {
            // Show an error message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: Failed to load recipe.")
            }
        }
    }
}
