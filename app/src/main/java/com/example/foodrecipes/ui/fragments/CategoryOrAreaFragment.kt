package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentCategoryOrAreaBinding
import com.example.foodrecipes.ui.adapters.MealAdapter
import com.example.foodrecipes.util.Resource

class CategoryOrAreaFragment : BaseFragment<FragmentCategoryOrAreaBinding>() {
    override val viewBinding: FragmentCategoryOrAreaBinding
        get() = FragmentCategoryOrAreaBinding.inflate(layoutInflater)

    private val args: CategoryOrAreaFragmentArgs by navArgs()
    private lateinit var mealAdapter: MealAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewMealsCategoryOrArea()
        observeForMeals()
        setAreaOrCategoryTitle()
    }

    private fun setAreaOrCategoryTitle() {
        if (args.category == "") {
            binding.tvCategoryAreaName.text = args.area
        }
        if (args.area == "") {
            binding.tvCategoryAreaName.text = args.category
        }
        if (args.category != "" && args.area != "") {
            binding.tvCategoryAreaName.text =
                getString(R.string.category_area, args.category, args.area)
        }
    }

    private fun observeForMeals() {
        viewModel.mealsByCategoryOrArea.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    throwErrorResponseWithToast("data",
                        viewModel.mealsByCategoryOrArea.value?.message)
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    mealAdapter.differ.submitList(response.data?.meals)
                }
            }
        }
    }

    private fun setRecyclerViewMealsCategoryOrArea() {
        if (args.category != "")
            viewModel.getMealsByCategory(args.category)
        if (args.area != "")
            viewModel.getMealsByArea(args.area)
        mealAdapter = MealAdapter()
        binding.rvMealsByCategoryOrArea.apply {
            adapter = mealAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        mealAdapter.itemClick = { meal ->
            if (isInternetAvailable()) {
                val action =
                    CategoryOrAreaFragmentDirections.actionCategoryOrAreaFragmentToMealDetailFragment(
                        meal.idMeal)
                findNavController().navigate(action)
            }
        }
    }

}