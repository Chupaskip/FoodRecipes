package com.example.foodrecipes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.foodrecipes.models.Meal

@Dao
interface FoodDao {
    @Upsert
    suspend fun upsertMeal(meal: Meal)
    @Delete
    suspend fun deleteMeal(meal:Meal)
    @Query("select * from meals")
    fun getFavoriteMeals(): LiveData<List<Meal>>
}