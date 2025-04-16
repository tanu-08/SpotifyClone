package com.tanu.spotifymusic.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.tanu.spotifymusic.R
import com.tanu.spotifymusic.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView, navController)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.libraryFragment -> {
                    navController.navigate(R.id.libraryFragment)
                    true
                }
                else -> false
            }
        }

        val topLevelDestinationIds: Set<Int> = setOf(
            R.id.homeFragment,
            R.id.searchFragment,
            R.id.libraryFragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("HomeBottomNav", destination.id.toString() + destination.label)
            binding.bottomNavigationView.isVisible =
                topLevelDestinationIds.contains(destination.id)
        }
    }
}