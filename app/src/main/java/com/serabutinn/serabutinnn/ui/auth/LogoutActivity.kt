package com.serabutinn.serabutinnn.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serabutinn.serabutinnn.databinding.ActivityLogoutBinding
import com.serabutinn.serabutinnn.utils.SessionManager

class LogoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogout.setOnClickListener {
            SessionManager.clearData(this)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }
}