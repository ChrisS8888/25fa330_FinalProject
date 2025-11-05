/*package com.example.myrecipepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myrecipepal.ui.theme.theme.MyRecipePalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyRecipePalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyRecipePalTheme {
        Greeting("Android")
    }
}*/
// In MainActivity.kt
package com.example.myrecipepal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myrecipepal.network.MealDbApi // <-- Import your API object
import com.example.myrecipepal.ui.theme.MyRecipePalTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyRecipePalTheme {
                // --- START OF TEMPORARY TEST CODE ---
                val scope = rememberCoroutineScope() // Coroutine scope for the button click

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        // When the button is clicked, launch a coroutine
                        scope.launch {
                            try {
                                // Use the API service you created!
                                val mealResponse = MealDbApi.retrofitService.getRecipesByCategory("Dessert")

                                // Print the number of meals found to Logcat
                                Log.d("MainActivity", "Success: ${mealResponse.meals.size} desserts found!")

                                // Print the name of the first dessert
                                if (mealResponse.meals.isNotEmpty()) {
                                    Log.d("MainActivity", "First dessert: ${mealResponse.meals.first().name}")
                                }

                            } catch (e: Exception) {
                                // If the network call fails, print the error
                                Log.e("MainActivity", "Failure: ${e.message}")
                            }
                        }
                    }) {
                        Text("Test API Call")
                    }
                }
                // --- END OF TEMPORARY TEST CODE ---
            }
        }
    }
}
