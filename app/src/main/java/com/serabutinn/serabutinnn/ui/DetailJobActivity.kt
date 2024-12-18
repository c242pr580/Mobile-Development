package com.serabutinn.serabutinnn.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobBinding
import com.serabutinn.serabutinnn.lightStatusBar
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
        lightStatusBar(window)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val id = intent.getStringExtra("id")
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.isSuccess.observe(this){
            if (it){
                viewModel.getSession().observe(this){viewModel.getJobDetailMitra(it.token,id.toString())}
            }
        }
        viewModel.data.observe(this) {
            val data = it
            binding.judulJob.text = data?.title
            binding.description.text = data?.description
            binding.location.text = data?.location
            binding.biaya.text = data?.cost?.let { it1 -> formatToRupiah(it1) }
            binding.tvStatus.text = data?.status
            binding.deadline.text = "Deadline | " + data?.deadline
            if(data?.image==null){
                Glide.with(binding.root)
                    .load(R.drawable.serabutinn_notext)
                    .centerCrop()
                    .into(binding.imgJob)
            }
            else{Glide.with(binding.root)
                .load(data.image)
                .centerCrop()
                .into(binding.imgJob)}
            if (data != null) {
                if (data.image == null) {
                    binding.imgJob.visibility = View.GONE
                }
                when (data.status) {
                    "Pending" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FFDA44"))
                        binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.btnUpdate.visibility = View.VISIBLE
                        showItems()
                    }

                    "In Progress" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FFA500"))
                        binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.btnUpdate.visibility = View.GONE
                        showItems()
                    }

                    "Completed" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ECFFEC"))
                        binding.tvStatus.setTextColor(Color.parseColor("#188018"))
                        binding.btnUpdate.visibility = View.GONE
                        showItems()
                    }
                    "Canceled"-> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FF0000"))
                        binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.btnUpdate.visibility = View.GONE
                        showItems()
                    }
                }
            }

            binding.ivWa.setOnClickListener {
                if (data != null) {
                    data.phone?.let { it1 ->
                        openWhatsApp(
                            it1,
                            "Halo, Saya tertarik dengan Pekerjaan ${data.title}"
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
                viewModel.getJobDetailMitra(token,id.toString())
            }
        }
    }

    private fun showItems() {
        binding.apply {
            btnBack.visibility = View.VISIBLE
            tvDetailJobs.visibility = View.VISIBLE
            ivWa.visibility = View.VISIBLE
            tvloc.visibility = View.VISIBLE
            tvdesc.visibility = View.VISIBLE
            imgJob.visibility = View.VISIBLE
            vwLine.visibility = View.VISIBLE
            cvStatus.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
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
            val encodedMessage = Uri.encode(message)
            val uri = Uri.parse("https://wa.me/62$phoneNumber?text=$encodedMessage")

            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}