package com.example.foodrecipes.repository

import com.example.foodrecipes.network.RetrofitInstance

class FoodRepository {
    suspend fun getRandomMeal() = RetrofitInstance.api.getRandomMeal()
    suspend fun getPopularMeals() = RetrofitInstance.api.getPopularMeals()
    suspend fun getCategories() = RetrofitInstance.api.getCategories()
    suspend fun getMealDetails(idMeal: String) = RetrofitInstance.api.getMealDetails(idMeal)
    suspend fun getSearchMeals(searchText: String) = RetrofitInstance.api.getSearchMeals(searchText)
    suspend fun getMealsByCategory(category:String) = RetrofitInstance.api.getMealsByCategory(category)
    suspend fun getMealsByArea(area:String) = RetrofitInstance.api.getMealsByArea(area)
}