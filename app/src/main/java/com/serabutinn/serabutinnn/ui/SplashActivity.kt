package com.serabutinn.serabutinnn.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivitySplashBinding
import com.serabutinn.serabutinnn.ui.auth.MainActivity2
import com.serabutinn.serabutinnn.ui.customerpage.HomeCustomerActivity
import com.serabutinn.serabutinnn.viewmodel.LoginViewModel
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.logoImageView.startAnimation(fadeInAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getSession().observe(this) { user ->
                if (user.isLogin) {
                    when (user.roleid) {
                        "1" -> {
                            val intent = Intent(this,HomeCustomerActivity::class.java)
                            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        "2" -> {
                            val intent = Intent(this,HomeActivity::class.java)
                            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                else{
                    val intent = Intent(this,MainActivity2::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
            finish()
        }, 2500)
    }
}