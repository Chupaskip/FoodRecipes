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

    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i")
        idMeal: String,
    ): Response<MealResponse>

    @GET("search.php")
    suspend fun getSearchMeals(
        @Query("s")
        searchText:String
    ):Response<MealResponse>

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c")
        category:String="",
    ):Response<MealResponse>

    @GET("filter.php")
    suspend fun getMealsByArea(
        @Query("a")
        area:String=""
    ):Response<MealResponse>
}