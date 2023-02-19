package com.example.foodrecipes.repository

import com.example.foodrecipes.network.RetrofitInstance

class FoodRepository {
    suspend fun getRandomMeal() = RetrofitInstance.api.getRandomMeal()
    suspend fun getPopularMeals() = RetrofitInstance.api.getPopularMeals()
    suspend fun getCategories() = RetrofitInstance.api.getCategories()
    suspend fun getMealDetails(idMeal:String) = RetrofitInstance.api.getMealDetails(idMeal)
    }