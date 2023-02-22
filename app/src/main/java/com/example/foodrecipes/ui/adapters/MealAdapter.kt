package com.example.foodrecipes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.ItemMealBinding
import com.example.foodrecipes.models.Meal

class MealAdapter : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {
    inner class MealViewHolder(val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    lateinit var itemClick:((Meal)->Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(ItemMealBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.binding.apply {
            Glide.with(this.root)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.img_placeholder)
                .into(imgMeal)
            tvMealName.text = meal.strMeal
        }
        holder.itemView.setOnClickListener{
            itemClick.invoke(meal)
        }
    }

    override fun getItemCount() = differ.currentList.size


}