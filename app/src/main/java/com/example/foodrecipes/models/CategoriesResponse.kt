package com.example.foodrecipes.models


import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    val categories: List<Category>
)