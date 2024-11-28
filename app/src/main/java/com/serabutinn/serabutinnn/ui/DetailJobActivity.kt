package com.serabutinn.serabutinnn.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobBinding


class DetailJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()


        binding.judulJob.text
        binding.namaCust.text
        binding.biaya.text
        binding.location.text
        binding.description.text
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.btnDaftar.setOnClickListener {}
        binding.imageButton4.setOnClickListener {}
        binding.tvStatus.text
//        binding.cvStatus.setCardBackgroundColor()
//        binding.imgJob.setImageResource()
    }
}