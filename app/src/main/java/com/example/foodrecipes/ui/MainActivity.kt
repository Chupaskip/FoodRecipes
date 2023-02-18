package com.example.foodrecipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.foodrecipes.R
import com.example.foodrecipes.repository.FoodRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController
        val viewModelProviderFactory = FoodViewModelFactory(FoodRepository())
        val viewModel = ViewModelProvider(this, viewModelProviderFactory)[FoodViewModel::class.java]
    }
}