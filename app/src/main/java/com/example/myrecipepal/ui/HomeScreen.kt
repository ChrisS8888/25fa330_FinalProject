package com.example.myrecipepal.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myrecipepal.R
import com.example.myrecipepal.ui.components.FoodPatternBackground

// A list of categories to display.
val foodCategories = listOf("Dessert", "Seafood", "Pasta", "Chicken", "Beef", "Vegetarian")

@Composable
fun HomeScreen(
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // --- 1. Define your Custom Pixel Font here ---
    // Ensure you have a file named 'pixel_font.ttf' inside 'res/font/'
    // If your file is named differently, change R.font.pixel_font to match.
    val pixelFont = androidx.compose.ui.text.font.FontFamily(Font(R.font.pixelfont))

    // Wrap the screen content in our custom background
    FoodPatternBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Replace the Text("My Recipe Pal") with this:
            Image(
                painter = painterResource(id = R.drawable.myrecipepal),
                contentDescription = "My Recipe Pal Logo",
                modifier = Modifier
                    .size(200.dp) // Adjust size as needed
                    .padding(bottom = 16.dp)
            )
            Text(
                text = "MyRecipePal",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = pixelFont,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "What are you cooking today?",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = pixelFont,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Create a button for each category
            foodCategories.forEach { category ->
                Button(
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier
                        .fillMaxWidth(0.7f) // Buttons take up 70% of screen width
                        .height(50.dp),     // Make buttons taller
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium
                        //fontFamily = pixelFont,
                        //color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

