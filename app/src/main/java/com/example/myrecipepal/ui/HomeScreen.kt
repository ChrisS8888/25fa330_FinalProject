// In ui/HomeScreen.kt
package com.example.myrecipepal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// A list of categories to display. You can expand this later.
val foodCategories = listOf("Dessert", "Seafood", "Pasta", "Chicken", "Beef", "Vegetarian")

@Composable
fun HomeScreen(
    // This is a function that will be called when a category is selected.
    // It will trigger the navigation.
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select a Category", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        // Create a button for each category
        foodCategories.forEach { category ->
            Button(onClick = { onCategorySelected(category) }) {
                Text(category)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
