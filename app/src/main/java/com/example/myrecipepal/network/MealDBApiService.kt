package com.example.myrecipepal.network

import com.example.myrecipepal.model.MealApiResponse
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

// Base URL for TheMealDB API
private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

// JSON configuration to ignore unknown keys from the API response
private val json = Json { ignoreUnknownKeys = true }

// The Retrofit object that will handle network requests
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

// The public interface defining the API endpoints
interface MealDbApiService {
    // Endpoint to get recipes by category
    @GET("filter.php")
    suspend fun getRecipesByCategory(@Query("c") category: String): MealApiResponse
}

// Public object to create and expose the Retrofit service
object MealDbApi {
    val retrofitService: MealDbApiService by lazy {
        retrofit.create(MealDbApiService::class.java)
    }
}
