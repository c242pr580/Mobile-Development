package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobCustomerBinding
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class DetailJobCustomerActivity : AppCompatActivity() {
    companion object{
        const val ID = "id"
    }
    private val viewModel by viewModels<DetailJobCustomerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailJobCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityDetailJobCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        binding.btnHapus.setOnClickListener {
            viewModel.deleteJob(id.toString())
            finish()
        }
        viewModel.getSession().observe(this){
            viewModel.getJobDetailCust(it.token,id.toString())
        }

    }
}