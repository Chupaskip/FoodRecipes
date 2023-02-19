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
        get() = _randomMeal

    private val _popularMeals = MutableLiveData<List<Meal>>()
    val popularMeals: LiveData<List<Meal>>
        get() = _popularMeals

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _mealDetails = MutableLiveData<Meal>()
    val mealDetails: LiveData<Meal>
        get() = _mealDetails

    private val _searchMeals = MutableLiveData<List<Meal>>()
    val searchMeals: LiveData<List<Meal>>
        get() = _searchMeals

    private val _mealsByCategoryOrArea = MutableLiveData<List<Meal>>()
    val mealsByCategoryOrArea: LiveData<List<Meal>>
        get() = _mealsByCategoryOrArea

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

    fun getMealDetails(idMeal: String) {
        viewModelScope.launch {
            val response = repository.getMealDetails(idMeal)
            _mealDetails.postValue(response.body()!!.meals[0])
        }
    }

    fun getSearchMeals(searchText: String) {
        viewModelScope.launch {
            val response = repository.getSearchMeals(searchText)
            _searchMeals.postValue(response.body()!!.meals)
        }
    }

    fun getMealsByCategory(category: String) {
        viewModelScope.launch {
            val response = repository.getMealsByCategory(category)
            _mealsByCategoryOrArea.postValue(response.body()!!.meals)
        }
    }

    fun getMealsByArea(area:String){
        viewModelScope.launch {
            val response = repository.getMealsByArea(area)
            _mealsByCategoryOrArea.postValue(response.body()!!.meals)
        }
    }
}