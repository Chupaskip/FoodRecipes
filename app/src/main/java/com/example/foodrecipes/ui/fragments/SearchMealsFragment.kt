package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.support.v4.os.IResultReceiver._Parcel
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.databinding.FragmentSearchMealsBinding
import com.example.foodrecipes.ui.MainActivity
import com.example.foodrecipes.ui.adapters.MealAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        viewModel.searchMeals.observe(viewLifecycleOwner) { meals ->
            mealAdapter.differ.submitList(meals)
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
        var job: Job? = null
        binding.etSearch.addTextChangedListener { searchText ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(500)
                Toast.makeText((activity as MainActivity), "Search", Toast.LENGTH_SHORT).show()
                viewModel.getSearchMeals(searchText.toString())
            }
        }
    }
}