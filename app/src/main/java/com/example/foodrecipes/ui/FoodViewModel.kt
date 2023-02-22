package com.example.foodrecipes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.models.CategoriesResponse
import com.example.foodrecipes.models.Meal
import com.example.foodrecipes.models.MealResponse
import com.example.foodrecipes.repository.FoodRepository
import com.example.foodrecipes.util.Resource
import kotlinx.coroutines.launch

class FoodViewModel(
    val repository: FoodRepository,
) : ViewModel() {

    private val _randomMeal = MutableLiveData<Resource<MealResponse>>()
    val randomMeal: LiveData<Resource<MealResponse>> = _randomMeal

    private val _popularMeals = MutableLiveData<Resource<MealResponse>>()
    val popularMeals: LiveData<Resource<MealResponse>> = _popularMeals

    private val _categories = MutableLiveData<Resource<CategoriesResponse>>()
    val categories: LiveData<Resource<CategoriesResponse>> = _categories

    private val _mealDetails = MutableLiveData<Resource<MealResponse>>()
    val mealDetails: LiveData<Resource<MealResponse>> = _mealDetails

    private val _searchMeals = MutableLiveData<Resource<MealResponse>>()
    val searchMeals: LiveData<Resource<MealResponse>> = _searchMeals

    private val _mealsByCategoryOrArea = MutableLiveData<Resource<MealResponse>>()
    val mealsByCategoryOrArea: LiveData<Resource<MealResponse>> = _mealsByCategoryOrArea


    init {
        setMainFragment()
    }

    fun setMainFragment() {
        getRandomMeal()
        getPopularMeals()
        getCategories()
    }

    private fun getRandomMeal() {
        _randomMeal.postValue(Resource.Loading())
        viewModelScope.launch {
            _randomMeal.postValue(repository.getRandomMeal())
        }
    }

    private fun getPopularMeals() {
        _popularMeals.postValue(Resource.Loading())
        viewModelScope.launch {
            _popularMeals.postValue(repository.getPopularMeals())
        }
    }

    private fun getCategories() {
        _categories.postValue(Resource.Loading())
        viewModelScope.launch {
            _categories.postValue(repository.getCategories())
        }
    }

    fun getMealDetails(idMeal: String) {
        _mealDetails.postValue(Resource.Loading())
        viewModelScope.launch {
            _mealDetails.postValue(repository.getMealDetails(idMeal))
        }
    }

    fun getSearchMeals(searchText: String) {
        _searchMeals.postValue(Resource.Loading())
        viewModelScope.launch {
           _searchMeals.postValue(repository.getSearchMeals(searchText))
        }
    }

    fun getMealsByCategory(category: String) {
        _mealsByCategoryOrArea.postValue(Resource.Loading())
        viewModelScope.launch {
            _mealsByCategoryOrArea.postValue(repository.getMealsByCategory(category))
        }
    }

    fun getMealsByArea(area: String) {
        _mealsByCategoryOrArea.postValue(Resource.Loading())
        viewModelScope.launch {
            _mealsByCategoryOrArea.postValue(repository.getMealsByArea(area))
        }
    }

    fun upsertMeal(meal: Meal){
        viewModelScope.launch {
            repository.upsertMeal(meal)
        }
    }

    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }



    fun getFavoriteMeals() = repository.getFavoriteMeals()

}