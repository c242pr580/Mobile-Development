package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
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
import com.serabutinn.serabutinnn.databinding.ActivityMainCustomerBinding
import com.serabutinn.serabutinnn.ui.customerpage.home.HomeCustomerFragment
import com.serabutinn.serabutinnn.ui.mitrapage.Profile.ProfileFragment
import com.serabutinn.serabutinnn.ui.mitrapage.dashboard.DashboardFragment

class HomeCustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerCust)
                if (currentFragment is HomeCustomerFragment) {
                    currentFragment.handleBackPress()
                } else {
                    finishAffinity() // Exit the app for other fragments
                }
            }
        })

        val navView: BottomNavigationView = binding.bottomNavigation

        loadFragment(HomeCustomerFragment())
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home_customer -> loadFragment(HomeCustomerFragment())
                R.id.navigation_dashboard2 -> loadFragment(DashboardFragment())
                R.id.navigation_notifications2 -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerCust, fragment)
            .commit()
    }
}