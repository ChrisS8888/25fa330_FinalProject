//// In model/Meal.kt
//package com.example.myrecipepal.model
//
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//// This class represents the entire JSON response from the API
//@Serializable
//data class MealApiResponse(
//    val meals: List<Meal>? = null
//)
//
//// This class represents a single recipe item in the list
//@Serializable
//data class Meal(
//    @SerialName("idMeal")
//    val id: String,
//
//    @SerialName("strMeal")
//    val name: String,
//
//    @SerialName("strMealThumb")
//    val thumbnail: String? = null, //CHANGED LINE 25 TO ALLOW FOR NULL, SO IF THERE IS NO PIC, WE IGHT
//
//
//    @SerialName("strInstructions")
//    val instructions: String? = null,
//
//    @SerialName("strCategory")
//    val category: String? = null
//
//)
// In model/Meal.kt
package com.example.myrecipepal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealApiResponse(
    val meals: List<Meal>?
)

@Serializable
data class Meal(
    @SerialName("idMeal")
    val id: String,
    @SerialName("strMeal")
    val name: String,
    @SerialName("strMealThumb")
    val thumbnail: String? = null,
    @SerialName("strInstructions")
    val instructions: String? = null,
    @SerialName("strCategory")
    val category: String? = null,

    // Ingredients
    @SerialName("strIngredient1") val strIngredient1: String? = null,
    @SerialName("strIngredient2") val strIngredient2: String? = null,
    @SerialName("strIngredient3") val strIngredient3: String? = null,
    @SerialName("strIngredient4") val strIngredient4: String? = null,
    @SerialName("strIngredient5") val strIngredient5: String? = null,
    @SerialName("strIngredient6") val strIngredient6: String? = null,
    @SerialName("strIngredient7") val strIngredient7: String? = null,
    @SerialName("strIngredient8") val strIngredient8: String? = null,
    @SerialName("strIngredient9") val strIngredient9: String? = null,
    @SerialName("strIngredient10") val strIngredient10: String? = null,
    @SerialName("strIngredient11") val strIngredient11: String? = null,
    @SerialName("strIngredient12") val strIngredient12: String? = null,
    @SerialName("strIngredient13") val strIngredient13: String? = null,
    @SerialName("strIngredient14") val strIngredient14: String? = null,
    @SerialName("strIngredient15") val strIngredient15: String? = null,
    @SerialName("strIngredient16") val strIngredient16: String? = null,
    @SerialName("strIngredient17") val strIngredient17: String? = null,
    @SerialName("strIngredient18") val strIngredient18: String? = null,
    @SerialName("strIngredient19") val strIngredient19: String? = null,
    @SerialName("strIngredient20") val strIngredient20: String? = null,

    // Measures
    @SerialName("strMeasure1") val strMeasure1: String? = null,
    @SerialName("strMeasure2") val strMeasure2: String? = null,
    @SerialName("strMeasure3") val strMeasure3: String? = null,
    @SerialName("strMeasure4") val strMeasure4: String? = null,
    @SerialName("strMeasure5") val strMeasure5: String? = null,
    @SerialName("strMeasure6") val strMeasure6: String? = null,
    @SerialName("strMeasure7") val strMeasure7: String? = null,
    @SerialName("strMeasure8") val strMeasure8: String? = null,
    @SerialName("strMeasure9") val strMeasure9: String? = null,
    @SerialName("strMeasure10") val strMeasure10: String? = null,
    @SerialName("strMeasure11") val strMeasure11: String? = null,
    @SerialName("strMeasure12") val strMeasure12: String? = null,
    @SerialName("strMeasure13") val strMeasure13: String? = null,
    @SerialName("strMeasure14") val strMeasure14: String? = null,
    @SerialName("strMeasure15") val strMeasure15: String? = null,
    @SerialName("strMeasure16") val strMeasure16: String? = null,
    @SerialName("strMeasure17") val strMeasure17: String? = null,
    @SerialName("strMeasure18") val strMeasure18: String? = null,
    @SerialName("strMeasure19") val strMeasure19: String? = null,
    @SerialName("strMeasure20") val strMeasure20: String? = null

) { // <<< The parenthesis for the constructor ends here

    // vvv Add the helper function inside the class body's curly braces {} vvv
    fun getIngredientsWithMeasures(): List<String> {
        val ingredients = mutableListOf<String>()
        val properties = listOf(
            strIngredient1 to strMeasure1, strIngredient2 to strMeasure2, strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4, strIngredient5 to strMeasure5, strIngredient6 to strMeasure6,
            strIngredient7 to strMeasure7, strIngredient8 to strMeasure8, strIngredient9 to strMeasure9,
            strIngredient10 to strMeasure10, strIngredient11 to strMeasure11, strIngredient12 to strMeasure12,
            strIngredient13 to strMeasure13, strIngredient14 to strMeasure14, strIngredient15 to strMeasure15,
            strIngredient16 to strMeasure16, strIngredient17 to strMeasure17, strIngredient18 to strMeasure18,
            strIngredient19 to strMeasure19, strIngredient20 to strMeasure20
        )

        for ((ingredient, measure) in properties) {
            // Check if the ingredient is not null or just whitespace
            if (!ingredient.isNullOrBlank()) {
                // Combine measure and ingredient, trimming whitespace from both
                ingredients.add("${measure?.trim() ?: ""} ${ingredient.trim()}".trim())
            }
        }
        return ingredients
    }
}

