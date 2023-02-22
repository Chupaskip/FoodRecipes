package com.example.foodrecipes.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentMealDetailBinding
import com.example.foodrecipes.models.Meal
import com.example.foodrecipes.util.Resource
import com.google.android.material.snackbar.Snackbar

class MealDetailFragment : BaseFragment<FragmentMealDetailBinding>() {
    override val viewBinding: FragmentMealDetailBinding
        get() = FragmentMealDetailBinding.inflate(layoutInflater)

    private val args: MealDetailFragmentArgs by navArgs()
    private var favoriteMeals: List<Meal> = listOf()
    private var mealFromDB: Meal? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMealDetails(args.idMeal)

        observeForMealInDB()
        setOnFabBackClick()
        setOnCategoryAndAreaClick()
        setOnFabPlayVideoClick()
    }

    private fun observeForMealInDB() {
        viewModel.getFavoriteMeals().observe(viewLifecycleOwner) { meals ->
            favoriteMeals = meals
            mealFromDB = favoriteMeals.find { m -> m.idMeal == args.idMeal }
            observeForMealDetails()
        }
    }

    private fun setFabFavorite(meal: Meal? = null) {
        binding.fabFavorites.apply {
            if (mealFromDB == null) {
                setOnClickListener {
                    if (favoriteMeals.find { m ->
                            m.idMeal == viewModel.mealDetails.value?.data?.meals?.get(0)?.idMeal
                        } == null) {
                        setFabFavoriteOnAddMeal(meal!!)
                    } else {
                        setFabFavoriteOnDelete(meal!!)
                    }
                }
            } else {
                setImageResource(R.drawable.ic_not_favorite)
                setOnClickListener {
                    if (mealFromDB != null)
                        setFabFavoriteOnDelete(mealFromDB!!)
                    else {
                        if (isInternetAvailable()) {
                            viewModel.getMealDetails(args.idMeal)
                            setFabFavoriteOnAddMeal(viewModel.mealDetails.value?.data?.meals?.get(0)!!)
                        }
                    }
                }
            }
        }

    }

    private fun setFabFavoriteOnDelete(meal: Meal) {
        binding.fabFavorites.apply {
            viewModel.deleteMeal(meal)
            Snackbar.make(requireView(),
                "Meal is deleted from favorites",
                Snackbar.LENGTH_SHORT)
                .show()
            setImageResource(R.drawable.ic_favorite)
        }
    }

    private fun setFabFavoriteOnAddMeal(meal: Meal) {
        binding.fabFavorites.apply {
            viewModel.upsertMeal(meal)
            Snackbar.make(requireView(),
                "Meal is added to favorites",
                Snackbar.LENGTH_SHORT)
                .show()
            setImageResource(R.drawable.ic_not_favorite)
        }
    }

    private fun setOnFabPlayVideoClick() {
        binding.fabWatch.setOnClickListener {
            if (isInternetAvailable()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealFromDB?.strYoutube))
                activity?.startActivity(intent)
            }
        }
    }

    private fun setOnCategoryAndAreaClick() {
        binding.cvCategoryName.setOnClickListener {
            if (isInternetAvailable()) {
                if (!isInternetAvailable())
                    return@setOnClickListener
                val action =
                    MealDetailFragmentDirections.actionMealDetailFragmentToCategoryOrAreaFragment(
                        mealFromDB?.strCategory
                            ?: "")
                findNavController().navigate(action)
            }
        }
        binding.cvLocation.setOnClickListener {
            if (isInternetAvailable()) {
                if (!isInternetAvailable())
                    return@setOnClickListener
                val action =
                    MealDetailFragmentDirections.actionMealDetailFragmentToCategoryOrAreaFragment("",
                        mealFromDB?.strArea ?: "")
                findNavController().navigate(action)
            }
        }
    }

    private fun setOnFabBackClick() {
        binding.fabBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeForMealDetails() {
        if (mealFromDB == null) {
            viewModel.mealDetails.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Error -> {
                        throwErrorResponseWithToast("meal's details",
                            viewModel.mealDetails.value?.message)
                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val meal = response.data?.meals?.get(0) ?: Meal()
                        setMealDetails(meal)
                        setFabFavorite(meal)
                    }
                }
            }
        } else {
            setMealDetails(mealFromDB)
            setFabFavorite()
        }
    }

    private fun setMealDetails(meal: Meal?) {
        binding.apply {
            Glide.with(this.root)
                .load(meal?.strMealThumb)
                .placeholder(R.drawable.img_placeholder)
                .into(imgMeal)
            tvCategoryName.text = meal?.strCategory
            tvLocation.text = meal?.strArea
            tvInstructions.text = meal?.strInstructions
            binding.collapsingToolbar.title = meal?.strMeal
        }
    }

}