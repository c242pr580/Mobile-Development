package com.serabutinn.serabutinnn.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobBinding
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DetailJobActivity : AppCompatActivity() {
    companion object {
        const val ID = "id"
    }

    private val viewModel by viewModels<DetailJobMitraViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailJobBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val id = intent.getStringExtra("id")
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.data.observe(this) {
            val data = it
            binding.judulJob.text = data?.title
            binding.description.text = "Deskripsi : \n" + data?.description
            binding.location.text = "Lokasi : " + data?.location
            binding.biaya.text = data?.cost?.let { it1 -> formatToRupiah(it1) }
            binding.tvStatus.text = data?.status
            binding.deadline.text = "Deadline : " + data?.deadline
            Glide.with(this)
                .load(data?.image)
                .centerInside()
                .into(binding.imgJob)

            binding.imageButton4.setOnClickListener {
                if (data != null) {
                    data.phone?.let { it1 ->
                        openWhatsApp(
                            it1,
                            "Halo saya tertarik dengan pekerjaan ${data.title}"
                        )
                    }
                }
            }
        }
        viewModel.getSession().observe(this) {
            val token = it.token
            viewModel.getJobDetailMitra(token, id.toString())
            binding.btnUpdate.setOnClickListener {
                viewModel.daftarKerja(token, id.toString())
                recreate()
            }

        }
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@DetailJobActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun formatToRupiah(number: String): String {
        val amount = number.toLongOrNull() ?: 0L
        val decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val decimalFormat = DecimalFormat("Rp #,###", decimalFormatSymbols)
        return decimalFormat.format(amount)
    }
    private fun openWhatsApp(phoneNumber: String, message: String) {
        try {
            // Encode the message
            val encodedMessage = Uri.encode(message)

            // Create the WhatsApp URL
            val url = "https://wa.me/$phoneNumber?text=$encodedMessage"

            // Create the Intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            // Start the activity
            intent.resolveActivity(packageManager)?.let {
                startActivity(intent)
            } ?: run {
                println("WhatsApp is not installed.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}