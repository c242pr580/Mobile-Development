package com.serabutinn.serabutinnn.ui.customerpage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobCustomerBinding
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DetailJobCustomerActivity : AppCompatActivity() {
    companion object {
        const val ID = "id"
    }

    private val viewModel by viewModels<DetailJobCustomerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailJobCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailJobCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        viewModel.data.observe(this) {
            val data = it
            binding.judulJob.text = data?.title
            binding.description.text = "Deskripsi : \n" + data?.description
            binding.location.text = "Lokasi : " + data?.location
            if (data != null) {
                binding.biaya.text = "Cost : " + data.cost?.let { it1 -> formatToRupiah(it1) }
            }
            binding.tvStatus.text = data?.status
            binding.deadline.text = "Deadline : " + data?.deadline
            Glide.with(this)
                .load(data?.image)
                .centerInside()
                .into(binding.imgJob)
            if (data != null) {
                if (data.image == null) {
                    binding.imgJob.visibility = View.GONE
                }
                if (data.status == "Pending") {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ffde21"))
                    binding.tvStatus.setTextColor(Color.parseColor("#000000"))
                    binding.btnCompleted.visibility = View.GONE
                } else if (data.status == "In Progress") {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#5ce65c"))
                    binding.tvStatus.setTextColor(Color.parseColor("#0f4d0f"))
                    binding.btnCompleted.visibility = View.VISIBLE
                    binding.btnUpdate.visibility = View.GONE
                    binding.btnHapus.visibility = View.GONE
                } else if (data.status == "Completed") {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#B2BEB5"))
                    binding.tvStatus.setTextColor(Color.parseColor("#36454F"))
                    binding.btnCompleted.visibility = View.GONE
                    binding.btnUpdate.visibility = View.GONE
                    binding.btnHapus.visibility = View.GONE
                }

            }
        }
        binding.btnHapus.setOnClickListener {
            viewModel.getSession().observe(this) {
                viewModel.deleteJob(id.toString(), it.token)
                finish()
            }

        }
        viewModel.getSession().observe(this) {
            viewModel.getJobDetailCust(it.token, id.toString())
        }
        binding.btnUpdate.setOnClickListener { }
        binding.btnCompleted.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)

        }
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@DetailJobCustomerActivity, HomeCustomerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun formatToRupiah(number: String): String {
        val amount = number.toLongOrNull() ?: 0L
        val decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val decimalFormat = DecimalFormat("Rp #,###", decimalFormatSymbols)
        return decimalFormat.format(amount)
    }
}