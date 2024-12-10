package com.serabutinn.serabutinnn.ui.customerpage

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
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
        val intent = intent
        val action = intent.action
        val data = intent.data
        if (Intent.ACTION_VIEW == action && data != null) {
            // The app was opened via a deep link
            val deepLink = data.toString()
            // Handle the deep link accordingly
            Log.d("DeepLink", "App opened with deep link: $deepLink")
            val moveIntent = Intent(this,CompletedJobActivity::class.java)
            moveIntent.putExtra("id",intent.getStringExtra("id").toString())
            startActivity(moveIntent)
        } else {
            // The app was opened without a deep link
            Log.d("DeepLink", "App opened without deep link")
        }
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCloseSnap.setOnClickListener {
            viewModel.getSession().observe(this){
                viewModel.getJobDetailCust(it.token,intent.getStringExtra("id").toString())
                viewModel.data.observe(this){data->
                    if(data?.status=="Completed"){
                        val intents=Intent(Intent.ACTION_VIEW, Uri.parse("serabutinn://transaction"))
                        intents.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Finish Transaction", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        viewModel.getSession().observe(this) { user ->viewModel.createPayment(user.token,intent.getStringExtra("id").toString())}
        viewModel.linkPayment.observe(this){
            binding.PaymentWebview.webViewClient = WebViewClient()
            openUrlFromWebView(it.toString())
            binding.PaymentWebview.settings.javaScriptEnabled = true
            binding.PaymentWebview.settings.supportZoom()

        }
    }
    private fun openUrlFromWebView(url: String) {
        val webView: WebView = binding.PaymentWebview
        webView.webViewClient = object : WebViewClient() {
            private val pd = ProgressDialog(this@PaymentActivity)

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val requestUrl = request.url.toString()
                return if (requestUrl.contains("gojek://") ||
                    requestUrl.contains("shopeeid://") ||
                    requestUrl.contains("//wsa.wallet.airpay.co.id/") ||
                    requestUrl.contains("/gopay/partner/") ||
                    requestUrl.contains("/shopeepay/")
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, request.url)
                    startActivity(intent)
                    true
                } else {
                    false
                }
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                pd.setMessage("loading")
                pd.show()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                pd.hide()
                super.onPageFinished(view, url)
            }
        }

        webView.settings.apply {
            loadsImagesAutomatically = true
            javaScriptEnabled = true
        }
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadUrl(url)
    }

}