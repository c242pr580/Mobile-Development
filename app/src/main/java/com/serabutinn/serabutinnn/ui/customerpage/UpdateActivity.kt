package com.serabutinn.serabutinnn.ui.customerpage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    companion object {
        const val ID = "id"
    }
    private lateinit var binding: ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}