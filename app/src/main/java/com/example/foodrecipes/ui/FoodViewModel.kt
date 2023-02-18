package com.example.foodrecipes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.models.Category
import com.example.foodrecipes.models.Meal
import com.example.foodrecipes.repository.FoodRepository
import kotlinx.coroutines.launch

class FoodViewModel(
    val repository: FoodRepository,
) : ViewModel() {
    private val _randomMeal = MutableLiveData<Meal>()
    val randomMeal: LiveData<Meal>
        get() = _randomMeal!!

    private val _popularMeals = MutableLiveData<List<Meal>>()
    val popularMeals: LiveData<List<Meal>>
        get() = _popularMeals!!

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    init {
        getRandomMeal()
        getPopularMeals()
        getCategories()
    }

    private fun getRandomMeal() {
        viewModelScope.launch {
            val response = repository.getRandomMeal()
            _randomMeal.postValue(response.body()!!.meals[0])
        }
    }

    private fun getPopularMeals() {
        viewModelScope.launch {
            val response = repository.getPopularMeals()
            _popularMeals.postValue(response.body()!!.meals)
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            val response = repository.getCategories()
            _categories.postValue(response.body()!!.categories)
        }
    }
}