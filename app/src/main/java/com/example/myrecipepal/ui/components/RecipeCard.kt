// In ui/components/RecipeCard.kt
package com.example.myrecipepal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myrecipepal.R
import com.example.myrecipepal.model.Meal
// NEEDED TO WRAP THE FAV BUTTON IN A BOX 
@Composable
fun RecipeCard(
    meal: Meal,
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // --- Start of the fix ---
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(meal.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = meal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_menu_report_image)
                )
                IconButton(
                    onClick = onFavoriteClick,
                    // Use align on the modifier inside the Box
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Yellow else Color.Gray
                    )
                }
            }
            // --- End of the fix ---

            Text(
                text = meal.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}




//old code
// In ui/components/RecipeCard.kt
//package com.example.myrecipepal.ui.components
//
//import androidx.compose.foundation.clickable // <-- IMPORT THIS
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource // <-- IMPORT THIS
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.example.myrecipepal.R
//import com.example.myrecipepal.model.Meal
//
//@Composable
//fun RecipeCard(
//    meal: Meal,
//    modifier: Modifier = Modifier,
//    isFavorite: Boolean, // FAVORITE BUTTON NEW <---- SUPER COOL
//    onFavoriteClick: () -> Unit,// FAVORITE BUTTON CLICK NEW <---- COOL
//    onClick: () -> Unit // <-- ADD THIS ONCLICK PARAMETER
//) {
//    Card(
//        modifier = modifier
//            .padding(8.dp)
//            .clickable { onClick() }, // <-- ADD THIS MODIFIER TO MAKE IT CLICKABLE
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//    ) {
//        Column {
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(meal.thumbnail)
//                    .crossfade(true)
//                    .build(),
//                contentDescription = meal.name,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(190.dp),
//                // Using a built-in Android drawable as a placeholder
//                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
//                error = painterResource(android.R.drawable.ic_menu_report_image)
//            )
//            IconButton(
//                onClick = onFavoriteClick,
//                modifier = Modifier.align(Alignment.TopEnd as Alignment.Horizontal)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Star, // NEW <---- SUPER COOL
//                    contentDescription = "Favorite", // NEW <---- COOL
//                    tint = if (isFavorite) Color.Yellow else Color.Gray // MAKE IT YELLOW IF YOU CLICK ON IT
//                )
//            }
//            Text(
//                text = meal.name,
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }
//}

