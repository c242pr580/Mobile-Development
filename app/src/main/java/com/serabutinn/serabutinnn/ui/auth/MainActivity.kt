package com.serabutinn.serabutinnn.ui.auth

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityMainBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.ui.HomeActivity
import com.serabutinn.serabutinnn.ui.customerpage.HomeCustomerActivity
import com.serabutinn.serabutinnn.utils.SessionManager
import com.serabutinn.serabutinnn.viewmodel.LoginViewModel
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        lightStatusBar(window)
        setContentView(view)
        viewModel.getSession().observe(this) { user ->
            if (user.roleid == "1") {
                val intent = Intent(this, HomeCustomerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            } else if (user.roleid == "2") {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }
        }
        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.loggedIn.observe(this) {
            if (it) {
                stopLoading()
                navigateToHome()
            } else {
                stopLoading()
                AlertDialog.Builder(this).apply {
                    setTitle("Oops!")
                    setMessage("Login Gagal")
                    setPositiveButton("OK") { _, _ ->
                    }
                }.show()
            }
        }

        binding.btnLogin.setOnClickListener {
            showLoading()
            doLogin()
        }

        binding.btnRegister.setOnClickListener {
            doSignup()
        }
    }

    private fun navigateToHome() {
        viewModel.getSession().observe(this) { user ->
            if (user.roleid == "1") {
                val intent = Intent(this, HomeCustomerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            } else if (user.roleid == "2") {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        viewModel.loginCustomer(email = email, password = pwd)
    }

    private fun doSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }
}
