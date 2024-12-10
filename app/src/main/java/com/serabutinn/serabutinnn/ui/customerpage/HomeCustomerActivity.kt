package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityMainCustomerBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.ui.customerpage.home.HomeCustomerFragment
import com.serabutinn.serabutinnn.ui.mitrapage.Profile.ProfileFragment
import com.serabutinn.serabutinnn.ui.mitrapage.dashboard.DashboardFragment
import com.serabutinn.serabutinnn.ui.mitrapage.home.HomeFragment

class HomeCustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainCustomerBinding
    private var currentFragmentId: Int = R.id.navigation_home_customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(window)

        supportActionBar?.hide()
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerCust)
                if (currentFragment is HomeFragment) {
                    currentFragment.handleBackPress()
                } else {
                    finishAffinity() // Exit the app for other fragments
                }
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home_customer -> {
                    currentFragmentId = R.id.navigation_home_customer
                    replaceFragment(HomeCustomerFragment())
                    true
                }
                R.id.navigation_dashboard2 -> {
                    currentFragmentId = R.id.navigation_dashboard2
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.navigation_notifications2 -> {
                    currentFragmentId = R.id.navigation_notifications2
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt("currentFragmentId", R.id.navigation_home_customer)
        }
        when (currentFragmentId) {
            R.id.navigation_home_customer -> replaceFragment(HomeCustomerFragment())
            R.id.navigation_dashboard2 -> replaceFragment(DashboardFragment())
            R.id.navigation_notifications2 -> replaceFragment(ProfileFragment())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentFragmentId", currentFragmentId)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerCust, fragment)
            .commit()
    }
}
