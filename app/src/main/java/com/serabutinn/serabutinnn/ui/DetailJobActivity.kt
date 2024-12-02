package com.serabutinn.serabutinnn.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobBinding
import com.serabutinn.serabutinnn.ui.customerpage.DetailJobCustomerViewModel
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory


class DetailJobActivity : AppCompatActivity() {
    companion object{
        const val ID = "id"
    }
    private val viewModel by viewModels<DetailJobCustomerViewModel> {
        ViewModelFactory.getInstance(this)}

    private lateinit var binding: ActivityDetailJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val id = intent.getStringExtra("id")
        viewModel.data.observe(this){
            val data = it
            binding.judulJob.text = data?.title
            binding.description.text = data?.description
            binding.location.text = data?.location
            binding.biaya.text = data?.cost
            binding.tvStatus.text = data?.status
            binding.deadline.text = data?.deadline
            Glide.with(this)
                .load(data?.image)
                .centerInside()
                .into(binding.imgJob)
        }
        viewModel.getSession().observe(this){
            viewModel.getJobDetailCust(it.token,id.toString())
        }

    }
}