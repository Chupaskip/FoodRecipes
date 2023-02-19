package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
        randomMealClick()
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

    private fun randomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToMealDetailFragment(viewModel.randomMeal.value!!.idMeal)
            findNavController().navigate(action)
        }

    }

    private fun observeForCategories() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.differ.submitList(categories)
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
        mealAdapter.itemClick = { meal ->
            val action = MainFragmentDirections.actionMainFragmentToMealDetailFragment(meal.idMeal)
            findNavController().navigate(action)
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