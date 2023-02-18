package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentMainBinding
import com.example.foodrecipes.ui.adapters.CategoryAdapter
import com.example.foodrecipes.ui.adapters.MealAdapter


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
    }

    private fun observeForCategories() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.differ.submitList(categories)
        }
    }

    private fun setUpRecyclerViewCategories() {
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun observeForPopularMeals() {
        viewModel.popularMeals.observe(viewLifecycleOwner) { meals ->
            mealAdapter.differ.submitList(meals)
        }
    }

    private fun setUpRecyclerViewPopularMeals() {
        mealAdapter = MealAdapter()
        binding.rvPopularMeals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mealAdapter
        }
    }

    private fun observeForRandomMeal() {
        viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
            Glide.with(this@MainFragment)
                .load(meal?.strMealThumb ?: "")
                .into(binding.imgRandomMeal)
            binding.tvRandomMealName.text = meal.strMeal
        }
    }
}