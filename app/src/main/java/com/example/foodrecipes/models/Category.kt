package com.example.foodrecipes.models


import com.google.gson.annotations.SerializedName

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryDescription: String,
    val strCategoryThumb: String
)