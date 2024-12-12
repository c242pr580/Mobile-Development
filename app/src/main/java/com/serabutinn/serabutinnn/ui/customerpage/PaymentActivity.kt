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
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.data.local.AppDataBase
import com.serabutinn.serabutinnn.data.local.Job
import com.serabutinn.serabutinnn.databinding.ActivityPaymentBinding
import com.serabutinn.serabutinnn.repository.JobRepository
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentActivity : AppCompatActivity() {
    private val viewModel by viewModels<PaymentViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var jobRepository: JobRepository
    private lateinit var binding: ActivityPaymentBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val jobDao = AppDataBase.getDatabase(applicationContext).jobDao()
        jobRepository = JobRepository(jobDao)
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
            viewModel.getSession().observe(this) { session ->
                viewModel.getJobDetailCust(session.token, intent.getStringExtra("id").toString())
                viewModel.data.observe(this) { data ->
                    val deepLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse("serabutinn://transaction?id=${intent.getStringExtra("id").toString()}"))
                    deepLinkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(deepLinkIntent)
                    finish() // Finish the current activity after starting the new one
                }
            }
        }
        viewModel.getSession().observe(this) { user ->
            GlobalScope.launch(Dispatchers.IO) {
                val job = jobRepository.getJobById(intent.getStringExtra("id").toString())
                if (job != null) {
                    Log.d("Job", "Job ID: ${job.job_id}")
                    withContext(Dispatchers.Main) {
                        binding.PaymentWebview.webViewClient = WebViewClient()
                        openUrlFromWebView(job.payment_link)
                        binding.PaymentWebview.settings.javaScriptEnabled = true
                        binding.PaymentWebview.settings.supportZoom()
                    }
                }else{
                    viewModel.createPayment(user.token,intent.getStringExtra("id").toString())}
                }
            }

        viewModel.linkPayment.observe(this){
            val jobDao = AppDataBase.getDatabase(applicationContext).jobDao()
            jobRepository = JobRepository(jobDao)
            GlobalScope.launch(Dispatchers.IO) {
                jobRepository.insertJob(Job(job_id = intent.getStringExtra("id").toString(), payment_link = it.toString()))
            }
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
    override fun onDestroy() {
        binding.PaymentWebview.apply {
            clearCache(true)
            clearHistory()
            removeAllViews()
            destroy()
        }
        super.onDestroy()
    }
}