package com.serabutinn.serabutinnn.ui.auth

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivitySignupBinding
import com.serabutinn.serabutinnn.ui.HomeActivity
import com.serabutinn.serabutinnn.utils.SessionManager
import com.serabutinn.serabutinnn.viewmodel.SignupViewModel
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory


class SignupActivity  : AppCompatActivity(){
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel.getSession().observe(this) { user ->
            Log.e("token",user.toString())
            if (user.isLogin) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }
        // Spinner Drop down elements
        val categories: MutableList<String> = ArrayList()
        categories.add("Customer")
        categories.add("Mitra")



        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        // attaching data adapter to spinner
        binding.spinner.adapter = dataAdapter
        viewModel.signed.observe(this) {
            stopLoading()
            if (it) {
                Toast.makeText(this, "Berhasil daftar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else{
                AlertDialog.Builder(this).apply {
                    setTitle("Oops!")
                    setMessage("Gagal Register")
                    setPositiveButton("OK") { _, _ ->
                        finish()
                    }
                }.show()
            }
        }
        viewModel.error.observe(this) {
            stopLoading()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        binding.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.btnRegister.setOnClickListener {
            showLoading()
            doSignup()
        }
    }
    private fun navigateToHome() {
        val intent = Intent(this, LogoutActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun doLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun doSignup() {
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        val name = binding.txtInputName.text.toString()
        val username = binding.txtInputUsername.text.toString()
        val roleId = if(binding.spinner.selectedItem.toString()=="Customer") 1 else 2
        val location = binding.txtInputLocation.text.toString()
        val phone = binding.txtInputPhone.text.toString()
        viewModel.signUp(email = email, password = pwd,name = name,username = username,roleid = roleId,location = location,phone = phone)
    }

    private fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

}