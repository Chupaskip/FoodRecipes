package com.example.foodrecipes.network

import com.example.foodrecipes.models.CategoriesResponse
import com.example.foodrecipes.models.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {
    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>

    @GET("filter.php")
    suspend fun getPopularMeals(
        @Query("i")
        string: String = "beef",
    ): Response<MealResponse>

    @GET("categories.php")
    suspend fun getCategories(): Response<CategoriesResponse>
}