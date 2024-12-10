package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityCompletedJobBinding
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class CompletedJobActivity : AppCompatActivity() {

    private val viewModel by viewModels<CompletedJobsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityCompletedJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var point: Int = 0
        binding = ActivityCompletedJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("id").toString()
        viewModel.getSession().observe(this) {
            viewModel.completeJob(it.token, id)
        }
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                point = rating.toInt()
                // This ensures the action is triggered by user interaction
                // Do something when the user clicks or changes the rating
                Log.d("Rating", "User rated: $rating")
            }
        }
        binding.btnSubmit.setOnClickListener {
            showLoading(true)
            if (point == 0) {
                showLoading(false)
                Toast.makeText(this, "Mohon Berikan Rating", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getSession().observe(this) { user ->
                    viewModel.completeJob(user.token, id)
                    viewModel.ratejobs(user.token, point.toString(), id)
                }
            }
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}