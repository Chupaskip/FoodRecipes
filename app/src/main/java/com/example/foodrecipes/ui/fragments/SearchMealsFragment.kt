package com.example.foodrecipes.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.databinding.FragmentSearchMealsBinding
import com.example.foodrecipes.ui.MainActivity
import com.example.foodrecipes.ui.adapters.MealAdapter
import com.example.foodrecipes.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil


class SearchMealsFragment : BaseFragment<FragmentSearchMealsBinding>() {
    override val viewBinding: FragmentSearchMealsBinding
        get() = FragmentSearchMealsBinding.inflate(layoutInflater)

    private lateinit var mealAdapter: MealAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearch()
        setRecyclerViewSearchMeals()
        observeForSearchMeals()
    }

    private fun observeForSearchMeals() {
        viewModel.searchMeals.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    throwErrorResponseWithToast("search response",
                        viewModel.searchMeals.value?.message)
                    swipeRefresher?.isRefreshing = false
                }
                is Resource.Loading -> {
                    swipeRefresher?.isRefreshing = true
                }
                is Resource.Success -> {
                    mealAdapter.differ.submitList(response.data?.meals)
                    swipeRefresher?.isRefreshing = false
                }
            }
        }
    }

    private fun setRecyclerViewSearchMeals() {
        mealAdapter = MealAdapter()
        binding.rvSearchMeals.apply {
            adapter = mealAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        mealAdapter.itemClick = { meal ->
            val action =
                SearchMealsFragmentDirections.actionSearchMealsFragmentToMealDetailFragment(meal.idMeal)
            findNavController().navigate(action)
        }
    }

    private fun setSearch() {
        binding.etSearch.requestFocus()
        UIUtil.showKeyboard(requireContext(), binding.etSearch)
        var job: Job? = null
        binding.etSearch.addTextChangedListener { searchText ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(500)
                viewModel.getSearchMeals(searchText.toString())
            }
        }
    }

    override fun setOnScrollRefresher() {
        viewModel.getSearchMeals(binding.etSearch.text.toString())
    }
}