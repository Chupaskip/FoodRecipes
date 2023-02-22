package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        binding.swipeRefresher.setOnRefreshListener {
            viewModel.setMainFragment()
        }
    }

    private fun setSearchClick() {
        binding.etSearch.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSearchMealsFragment()
            findNavController().navigate(action)
        }
    }

    private fun randomMealClick(meal: Meal?) {
        binding.imgRandomMeal.setOnClickListener {
            if (!isInternetAvailable())
                return@setOnClickListener
            val action =
                MainFragmentDirections.actionMainFragmentToMealDetailFragment(meal?.idMeal
                    ?: "")
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
                    binding.swipeRefresher.isRefreshing = false
                    binding.tvCategories.isVisible = false
                    binding.rvCategories.isVisible = false
                }
                is Loading -> {
                    binding.swipeRefresher.isRefreshing = true
                }
                is Success -> {
                    categoryAdapter.differ.submitList(response.data?.categories)
                    binding.swipeRefresher.isRefreshing = false
                    binding.tvCategories.isVisible = true
                    binding.rvCategories.isVisible = true

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
            if (isInternetAvailable()) {
                val action =
                    MainFragmentDirections.actionMainFragmentToCategoryOrAreaFragment(category.strCategory)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeForPopularMeals() {
        viewModel.popularMeals.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Error -> {
                    throwErrorResponseWithToast(
                        "popular meals",
                        viewModel.popularMeals.value?.message)
                    binding.swipeRefresher.isRefreshing = false
                    binding.tvPopularMeals.isVisible = false
                    binding.rvPopularMeals.isVisible = false
                }
                is Loading -> {
                    binding.swipeRefresher.isRefreshing = true
                }
                is Success -> {
                    mealAdapter.differ.submitList(response.data?.meals)
                    binding.swipeRefresher.isRefreshing = false
                    binding.rvPopularMeals.isVisible = true
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
            if (isInternetAvailable()) {
                val action =
                    MainFragmentDirections.actionMainFragmentToMealDetailFragment(meal.idMeal)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeForRandomMeal() {
        viewModel.randomMeal.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Error -> {
                    throwErrorResponseWithToast(
                        "random meal",
                        viewModel.randomMeal.value?.message)
                    binding.apply {
                        imgCard.isVisible = false
                        swipeRefresher.isRefreshing = false
                        tvRandomMeal.isVisible = false
                    }
                }
                is Loading -> {
                    binding.swipeRefresher.isRefreshing = true
                }
                is Success -> {
                    val meal = response.data?.meals?.get(0)
                    Glide.with(this@MainFragment)
                        .load(meal?.strMealThumb ?: "")
                        .placeholder(R.drawable.img_placeholder)
                        .into(binding.imgRandomMeal)
                    binding.tvRandomMealName.text = meal?.strMeal
                    randomMealClick(meal)
                    binding.swipeRefresher.isRefreshing = false
                    binding.apply {
                        imgCard.isVisible = true
                        tvRandomMeal.isVisible = true
                    }
                }
            }
        }
    }

}