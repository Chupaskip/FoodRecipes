package com.example.foodrecipes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodrecipes.databinding.FragmentMealDetailBinding

class MealDetailFragment : BaseFragment<FragmentMealDetailBinding>() {
    override val viewBinding: FragmentMealDetailBinding
        get() = FragmentMealDetailBinding.inflate(layoutInflater)

    private val args: MealDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMealDetails(args.idMeal)

        observeForMealDetails()
        setOnFabBackClick()
        setOnCategoryAndAreaClick()
    }

    private fun setOnCategoryAndAreaClick() {
        binding.cvCategoryName.setOnClickListener {
            val action =
                MealDetailFragmentDirections.actionMealDetailFragmentToCategoryOrAreaFragment(viewModel.mealDetails.value?.strCategory
                    ?: "")
            findNavController().navigate(action)
        }
        binding.cvLocation.setOnClickListener {
            val action =
                MealDetailFragmentDirections.actionMealDetailFragmentToCategoryOrAreaFragment("",
                    viewModel.mealDetails.value?.strArea ?: "")
            findNavController().navigate(action)
        }
    }

    private fun setOnFabBackClick() {
        binding.fabBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeForMealDetails() {
        viewModel.mealDetails.observe(viewLifecycleOwner) { meal ->
            binding.apply {
                Glide.with(this.root)
                    .load(meal.strMealThumb)
                    .into(imgMeal)
                tvCategoryName.text = meal.strCategory
                tvLocation.text = meal.strArea
                tvInstructions.text = meal.strInstructions
                binding.collapsingToolbar.title = meal.strMeal
            }
        }
    }
}