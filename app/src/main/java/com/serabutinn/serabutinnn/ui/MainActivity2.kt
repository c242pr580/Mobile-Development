package com.serabutinn.serabutinnn.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityMain2Binding
import com.serabutinn.serabutinnn.ui.auth.MainActivity
import com.serabutinn.serabutinnn.ui.auth.SignupActivity

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnregisters.setOnClickListener {navigateToRegis()}
        binding.btnlogins.setOnClickListener {navigateToLogin()}

    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun navigateToRegis() {
        val intent = Intent(this, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }
}