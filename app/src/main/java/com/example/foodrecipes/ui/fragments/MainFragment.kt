package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentMainBinding
import com.example.foodrecipes.models.Meal
import com.example.foodrecipes.ui.adapters.CategoryAdapter
import com.example.foodrecipes.ui.adapters.MealAdapter
import com.example.foodrecipes.util.Resource.*


class MainFragment : BaseFragment<FragmentMainBinding>() {
    override val viewBinding: FragmentMainBinding
        get() = FragmentMainBinding.inflate(layoutInflater)

    private lateinit var mealAdapter: MealAdapter
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeForRandomMeal()
        setUpRecyclerViewPopularMeals()
        observeForPopularMeals()
        setUpRecyclerViewCategories()
        observeForCategories()
        setSearchClick()

    }

    private fun setSearchClick() {
        binding.etSearch.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSearchMealsFragment()
            findNavController().navigate(action)
        }
    }

    private fun randomMealClick(meal: Meal?) {
        binding.imgRandomMeal.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToMealDetailFragment(meal?.idMeal ?: "")
            findNavController().navigate(action)
        }
    }

    private fun observeForCategories() {
        viewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Error -> {
                    throwErrorResponseWithToast(
                        "categories",
                        viewModel.categories.value?.message)
                    swipeRefresher?.isRefreshing = false
                    binding.tvCategories.isVisible = false
                }
                is Loading -> {
                    swipeRefresher?.isRefreshing = true
                }
                is Success -> {
                    categoryAdapter.differ.submitList(response.data?.categories)
                    swipeRefresher?.isRefreshing = false
                    binding.tvCategories.isVisible = true
                }
            }
        }
    }

    private fun setUpRecyclerViewCategories() {
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        categoryAdapter.onItemClick = { category ->
            val action =
                MainFragmentDirections.actionMainFragmentToCategoryOrAreaFragment(category.strCategory)
            findNavController().navigate(action)
        }
    }

    private fun observeForPopularMeals() {
        viewModel.popularMeals.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Error -> {
                    throwErrorResponseWithToast(
                        "popular meals",
                        viewModel.popularMeals.value?.message)
                    swipeRefresher?.isRefreshing = false
                    binding.tvPopularMeals.isVisible = false
                }
                is Loading -> {
                    swipeRefresher?.isRefreshing = true
                }
                is Success -> {
                    mealAdapter.differ.submitList(response.data?.meals)
                    swipeRefresher?.isRefreshing = false
                    binding.tvPopularMeals.isVisible = true
                }
            }
        }
    }

    private fun setUpRecyclerViewPopularMeals() {
        mealAdapter = MealAdapter()
        binding.rvPopularMeals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mealAdapter
        }
        mealAdapter.itemClick = { meal ->
            val action = MainFragmentDirections.actionMainFragmentToMealDetailFragment(meal.idMeal)
            findNavController().navigate(action)
        }
    }

    private fun observeForRandomMeal() {
        viewModel.randomMeal.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Error -> {
                    throwErrorResponseWithToast(
                        "random meal",
                        viewModel.randomMeal.value?.message)
                    swipeRefresher?.isRefreshing = false
                    binding.apply {
                        imgCard.isVisible = false
                        tvRandomMeal.isVisible = false
                    }
                }
                is Loading -> {
                    swipeRefresher?.isRefreshing = true
                }
                is Success -> {
                    val meal = response.data?.meals?.get(0)
                    Glide.with(this@MainFragment)
                        .load(meal?.strMealThumb ?: "")
                        .error(R.drawable.img_error)
                        .into(binding.imgRandomMeal)
                    binding.tvRandomMealName.text = meal?.strMeal
                    randomMealClick(meal)
                    swipeRefresher?.isRefreshing = false
                    binding.apply {
                        imgCard.isVisible = true
                        tvRandomMeal.isVisible = true
                    }
                }
            }
        }
    }

    override fun setOnScrollRefresher() {
        viewModel.setMainFragment()
    }
}