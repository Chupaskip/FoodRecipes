package com.example.foodrecipes.ui.fragments

import com.example.foodrecipes.databinding.FragmentMealDetailBinding

class MealDetailFragment:BaseFragment<FragmentMealDetailBinding>() {
    override val viewBinding: FragmentMealDetailBinding
        get() = FragmentMealDetailBinding.inflate(layoutInflater)
}