package com.example.foodrecipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodrecipes.R
import com.example.foodrecipes.database.FoodDatabase
import com.example.foodrecipes.databinding.ActivityMainBinding
import com.example.foodrecipes.repository.FoodRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController
        val repository = FoodRepository(FoodDatabase(this))
        val viewModelProviderFactory = FoodViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelProviderFactory)[FoodViewModel::class.java]
        binding.bottomNavigationMenu.setupWithNavController(navController)
    }
}