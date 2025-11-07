// In model/Meal.kt
package com.example.myrecipepal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// This class represents the entire JSON response from the API
@Serializable
data class MealApiResponse(
    val meals: List<Meal>
)

// This class represents a single recipe item in the list
@Serializable
data class Meal(
    @SerialName("idMeal")
    val id: String,

    @SerialName("strMeal")
    val name: String,

    @SerialName("strMealThumb")
    val thumbnail: String? = null //CHANGED LINE 25 TO ALLOW FOR NULL, SO IF THERE IS NO PIC, WE IGHT
)

// OLD CODE BELOW
// JUST IN CASE YOU WANT TO COMPARE

//// In model/Meal.kt
//package com.example.myrecipepal.model
//
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//// This class represents the entire JSON response from the API
//@Serializable
//data class MealApiResponse(
//    val meals: List<Meal>
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
//    val thumbnail: String
//)