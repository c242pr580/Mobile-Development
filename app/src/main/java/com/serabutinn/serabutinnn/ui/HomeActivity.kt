package com.serabutinn.serabutinnn.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityHomeBinding
import com.serabutinn.serabutinnn.ui.customerpage.home.HomeCustomerFragment
import com.serabutinn.serabutinnn.ui.mitrapage.Profile.ProfileFragment
import com.serabutinn.serabutinnn.ui.mitrapage.history.HistoryFragment
import com.serabutinn.serabutinnn.ui.mitrapage.home.HomeFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                if (currentFragment is HomeFragment) {
                    currentFragment.handleBackPress()
                } else {
                    finishAffinity() // Exit the app for other fragments
                }
            }
        })

        val navView: BottomNavigationView = binding.bottomNavigation

        loadFragment(HomeFragment())
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> loadFragment(HomeFragment())
                R.id.navigation_dashboard -> loadFragment(HistoryFragment())
                R.id.navigation_notifications -> loadFragment(ProfileFragment())
            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}