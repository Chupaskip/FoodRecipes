package com.example.foodrecipes.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.FragmentMealDetailBinding
import com.example.foodrecipes.models.Meal
import com.example.foodrecipes.util.Resource

class MealDetailFragment : BaseFragment<FragmentMealDetailBinding>() {
    override val viewBinding: FragmentMealDetailBinding
        get() = FragmentMealDetailBinding.inflate(layoutInflater)

    private val args: MealDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMealDetails(args.idMeal)

        observeForMealDetails()
        setOnFabBackClick()
    }

    private fun setOnFabPlayVideoClick(meal: Meal) {
        binding.fabWatch.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube))
            activity?.startActivity(intent)
        }
    }

    private fun setOnCategoryAndAreaClick(meal: Meal) {
        binding.cvCategoryName.setOnClickListener {
            val action =
                MealDetailFragmentDirections.actionMealDetailFragmentToCategoryOrAreaFragment(meal.strCategory
                    ?: "")
            findNavController().navigate(action)
        }
        binding.cvLocation.setOnClickListener {
            val action =
                MealDetailFragmentDirections.actionMealDetailFragmentToCategoryOrAreaFragment("",
                    meal.strArea ?: "")
            findNavController().navigate(action)
        }
    }

    private fun setOnFabBackClick() {
        binding.fabBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeForMealDetails() {
        viewModel.mealDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    throwErrorResponseWithToast( "meal's details", viewModel.mealDetails.value?.message)
                    swipeRefresher?.isRefreshing = false
                }
                is Resource.Loading -> {
                    swipeRefresher?.isRefreshing = true
                }
                is Resource.Success -> {
                    val meal = response.data?.meals?.get(0) ?: Meal()
                    binding.apply {
                        Glide.with(this.root)
                            .load(meal.strMealThumb)
                            .error(R.drawable.img_error)
                            .into(imgMeal)
                        tvCategoryName.text = meal.strCategory
                        tvLocation.text = meal.strArea
                        tvInstructions.text = meal.strInstructions
                        binding.collapsingToolbar.title = meal.strMeal
                    }
                    setOnCategoryAndAreaClick(meal)
                    setOnFabPlayVideoClick(meal)
                    swipeRefresher?.isRefreshing = false
                }
            }
        }
    }

    override fun setOnScrollRefresher() {
        viewModel.getMealDetails(args.idMeal)
    }
}