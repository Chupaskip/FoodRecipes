package com.example.foodrecipes.models


import com.google.gson.annotations.SerializedName

data class MealResponse(
    val meals: List<Meal>
)