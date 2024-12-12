package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import com.serabutinn.serabutinnn.ui.mitrapage.history.HistoryFragment
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityMainCustomerBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.ui.customerpage.home.HomeCustomerFragment
import com.serabutinn.serabutinnn.ui.mitrapage.Profile.ProfileFragment

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
                if (currentFragment is HomeCustomerFragment) {
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
                R.id.navigation_history2 -> {
                    currentFragmentId = R.id.navigation_history2
                    replaceFragment(HistoryFragment())
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
            R.id.navigation_history2 -> replaceFragment(HistoryFragment())
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
