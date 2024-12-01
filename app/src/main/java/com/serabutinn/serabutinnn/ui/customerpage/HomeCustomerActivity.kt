package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityHomeBinding
import com.serabutinn.serabutinnn.databinding.ActivityMainCustomerBinding

class HomeCustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        enableEdgeToEdge()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main_customer)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_customer, R.id.navigation_dashboard2, R.id.navigation_notifications2
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}