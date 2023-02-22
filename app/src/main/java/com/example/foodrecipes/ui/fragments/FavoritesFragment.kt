package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.databinding.FragmentFavoritesBinding
import com.example.foodrecipes.ui.adapters.MealAdapter
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {
    override val viewBinding: FragmentFavoritesBinding
        get() = FragmentFavoritesBinding.inflate(layoutInflater)

    private lateinit var mealAdapter: MealAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFavoriteMealRecyclerView()
        observeForFavoriteMeals()
        setDeletingFromRvWithSwipe()
    }

    private fun setDeletingFromRvWithSwipe() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = mealAdapter.differ.currentList[position]
                viewModel.deleteMeal(meal)
                Snackbar.make(requireView(), "Meal id deleted successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.upsertMeal(meal)
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteMeals)
        }
    }

    private fun observeForFavoriteMeals() {
        viewModel.getFavoriteMeals()?.observe(viewLifecycleOwner) { meals ->
            mealAdapter.differ.submitList(meals)
        }
    }

    private fun setFavoriteMealRecyclerView() {
        mealAdapter = MealAdapter()
        binding.rvFavoriteMeals.apply {
            adapter = mealAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        mealAdapter.itemClick = { meal ->
            val action =
                FavoritesFragmentDirections.actionFavoritesFragmentToMealDetailFragment(meal.idMeal)
            findNavController().navigate(action)
        }
    }
}