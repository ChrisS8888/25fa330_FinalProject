package com.example.myrecipepal.database


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String?,
    val strInstructions: String?,
    val strCategory: String?
)
