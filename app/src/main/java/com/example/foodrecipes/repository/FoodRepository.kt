package com.example.foodrecipes.repository

import com.example.foodrecipes.database.FoodDatabase
import com.example.foodrecipes.models.Meal
import com.example.foodrecipes.network.RetrofitInstance

class FoodRepository(
    private val foodDatabase: FoodDatabase
) : BaseRepository() {
    suspend fun getRandomMeal() = safeApiCall { RetrofitInstance.api.getRandomMeal() }
    suspend fun getPopularMeals() =  safeApiCall { RetrofitInstance.api.getPopularMeals()}
    suspend fun getCategories() =  safeApiCall { RetrofitInstance.api.getCategories()}
    suspend fun getMealDetails(idMeal: String) =  safeApiCall { RetrofitInstance.api.getMealDetails(idMeal)}
    suspend fun getSearchMeals(searchText: String) =  safeApiCall { RetrofitInstance.api.getSearchMeals(searchText)}
    suspend fun getMealsByCategory(category: String) = safeApiCall { RetrofitInstance.api.getMealsByCategory(category)}
    suspend fun getMealsByArea(area: String) =  safeApiCall { RetrofitInstance.api.getMealsByArea(area)}

    suspend fun upsertMeal(meal: Meal) = foodDatabase.getFoodDao().upsertMeal(meal)
    suspend fun deleteMeal(meal:Meal)= foodDatabase.getFoodDao().deleteMeal(meal)
    fun getFavoriteMeals() = foodDatabase.getFoodDao().getFavoriteMeals()
}