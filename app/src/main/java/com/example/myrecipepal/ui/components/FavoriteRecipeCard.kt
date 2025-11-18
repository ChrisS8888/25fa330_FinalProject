package com.example.myrecipepal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myrecipepal.model.Meal

@Composable
fun FavoriteRecipeCard(
    meal: Meal,
    onCardClick: () -> Unit,
    onRemoveFavorite: () -> Unit, // This card only knows how to remove
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
    ) {
        Column {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(meal.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image of ${meal.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                // Favorite Icon is always at the top right
                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    // This IconButton only calls onRemoveFavorite
                    IconButton(onClick = onRemoveFavorite) {
                        Icon(
                            imageVector = Icons.Default.Star, // Always filled
                            contentDescription = "Remove from Favorites",
                            tint = Color.Yellow
                        )
                    }
                }
            }
            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}