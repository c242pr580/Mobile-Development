package com.serabutinn.serabutinnn.ui.auth

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.data.api.response.BaseResponse
import com.serabutinn.serabutinnn.data.api.response.LoginResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.databinding.ActivitySignupBinding
import com.serabutinn.serabutinnn.utils.SessionManager
import com.serabutinn.serabutinnn.viewmodel.SignupViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }

        viewModel.signupResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processSignup(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            doLogin()

        }

        binding.btnRegister.setOnClickListener {
            doSignup()
        }

    }

    private fun navigateToHome() {
        val intent = Intent(this, LogoutActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun doLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun doSignup() {
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        val name = binding.txtInputName.text.toString()
        val username = binding.txtInputUsername.text.toString()
        val roleId = "0"
        val location = binding.txtInputLocation.text.toString()
        val phone = binding.txtInputPhone.text.toString()
        viewModel.signupUser(email = email, pwd = pwd,name = name,username = username,role_id = roleId,location = location,phone = phone)
    }

    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    fun processLogin(data: LoginResponse?) {
        showToast("Success:" + data?.message)
        if (!data?.data?.token.isNullOrEmpty()) {
            data?.data?.token?.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
        }
    }

    fun processSignup(data: SignupResponse?) {
        if (data?.code!=201) {
            showToast("Success:" + data?.message)
            doLogin()
        }else{
            processError(data.message)
        }
    }

    fun processError(msg: String?) {
        showToast("Error:" + msg)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}