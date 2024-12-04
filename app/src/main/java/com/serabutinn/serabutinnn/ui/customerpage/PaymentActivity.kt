package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityPaymentBinding
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class PaymentActivity : AppCompatActivity() {
    private val viewModel by viewModels<PaymentViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSession().observe(this) { user ->viewModel.createPayment(user.token,intent.getStringExtra("id").toString())}
        viewModel.linkPayment.observe(this){
            binding.PaymentWebview.webViewClient = WebViewClient()
            binding.PaymentWebview.loadUrl(it.toString())
            binding.PaymentWebview.settings.javaScriptEnabled = true
            binding.PaymentWebview.settings.supportZoom()
        }

//        viewModel
//

    }
}