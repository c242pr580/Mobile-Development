package com.serabutinn.serabutinnn.ui.customerpage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityCompletedJobBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class CompletedJobActivity : AppCompatActivity() {

    private val viewModel by viewModels<CompletedJobsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityCompletedJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar(window)
        var point: Int = 0
        binding = ActivityCompletedJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var id= ""
        binding.ratingBar.rating = 0f
        viewModel.getSession().observe(this) {
            viewModel.completeJob(it.token, id)
        }
        val data: Uri? = intent?.data

        if (data != null) {
            id = data.getQueryParameter("id").toString()
        }else{
            Log.d("Data", "Data is null")
        }
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                point = rating.toInt()
                Log.d("Rating", "User rated: $rating")
            }
        }
        binding.btnSubmit.setOnClickListener {
            showLoading(true)
            if (point == 0) {
                showLoading(false)
                Toast.makeText(this, "Fill up Rating", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getSession().observe(this) { user ->
                    viewModel.ratejobs(user.token,point.toString(), id)
                    viewModel.isSuccess.observe(this) {viewModel.ratejobs(user.token, point.toString(), id)}

                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.isSuccess2.observe(this) {
            if (it) {
                val intent2= Intent(this, DetailJobCustomerActivity::class.java)
                intent2.putExtra("id",id)
                intent2.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent2)
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}