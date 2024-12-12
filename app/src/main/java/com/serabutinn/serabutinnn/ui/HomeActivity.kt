package com.serabutinn.serabutinnn.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityHomeBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.ui.mitrapage.history.HistoryFragment
import com.serabutinn.serabutinnn.ui.mitrapage.Profile.ProfileFragment
import com.serabutinn.serabutinnn.ui.mitrapage.home.HomeFragment
import com.serabutinn.serabutinnn.ui.mitrapage.jobs.JobsFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var currentFragmentId: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(window)

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

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    currentFragmentId = R.id.navigation_home
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_jobs -> {
                    currentFragmentId = R.id.navigation_jobs
                    replaceFragment(JobsFragment())
                    true
                }
                R.id.navigation_history -> {
                    currentFragmentId = R.id.navigation_history
                    replaceFragment(HistoryFragment())
                    true
                }
                R.id.navigation_notifications -> {
                    currentFragmentId = R.id.navigation_notifications
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt("currentFragmentId", R.id.navigation_home)
        }
        when (currentFragmentId) {
            R.id.navigation_home -> replaceFragment(HomeFragment())
            R.id.navigation_history -> replaceFragment(HistoryFragment())
            R.id.navigation_jobs -> replaceFragment(JobsFragment())
            R.id.navigation_notifications -> replaceFragment(ProfileFragment())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentFragmentId", currentFragmentId)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
