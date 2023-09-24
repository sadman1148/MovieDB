package com.techetronventures.moviedb.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.databinding.ActivityMainBinding
import com.techetronventures.moviedb.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference =  getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.movieDetailFragment
                || destination.id == R.id.searchFragment
                || destination.id == R.id.trailerFragment
                || destination.id == R.id.showDetailFragment
                || destination.id == R.id.favoriteFragment) {
                binding.bottomNavigationView.visibility = View.GONE
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
            supportActionBar?.title = destination.label
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_movie -> {
                    navController.navigate(R.id.movieFragment)
                    true
                }
                R.id.menu_item_shows -> {
                    navController.navigate(R.id.showFragment)
                    true
                }
                R.id.menu_item_graph -> {
                    navController.navigate(R.id.graphFragment)
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.theme -> {
            if (sharedPreference.getBoolean(Constants.KEY_IS_DARK_THEME, false)) {
                sharedPreference.edit().putBoolean(Constants.KEY_IS_DARK_THEME, false).apply()
                item.icon = AppCompatResources.getDrawable(this, R.drawable.night)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                sharedPreference.edit().putBoolean(Constants.KEY_IS_DARK_THEME, true).apply()
                item.icon = AppCompatResources.getDrawable(this, R.drawable.day)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            true
        }

        R.id.favorite -> {
            if (navController.currentDestination?.id != R.id.favoriteFragment) {
                navController.navigate(R.id.favoriteFragment)
            }
            true
        }

        R.id.search -> {
            if (navController.currentDestination?.id != R.id.searchFragment) {
                navController.navigate(R.id.searchFragment)
            }
            true
        }

        android.R.id.home -> {
            navController.navigateUp()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val themeItem = menu.findItem(R.id.theme)
        if (sharedPreference.getBoolean(Constants.KEY_INIT_LAUNCH, true)) {
            sharedPreference.edit().putBoolean(Constants.KEY_INIT_LAUNCH, false).apply()
            if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES) {
                themeItem.icon = ContextCompat.getDrawable(this, R.drawable.day)
                sharedPreference.edit().putBoolean(Constants.KEY_IS_DARK_THEME, true).apply()
            } else {
                themeItem.icon = ContextCompat.getDrawable(this, R.drawable.night)
                sharedPreference.edit().putBoolean(Constants.KEY_IS_DARK_THEME, false).apply()
            }
        } else {
            if (sharedPreference.getBoolean(Constants.KEY_IS_DARK_THEME, false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeItem.icon = ContextCompat.getDrawable(this, R.drawable.day)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeItem.icon = ContextCompat.getDrawable(this, R.drawable.night)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun setTheme(theme: Resources.Theme?) {
        super.setTheme(theme)
        if (sharedPreference.getBoolean(Constants.KEY_IS_DARK_THEME, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}