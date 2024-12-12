package com.serabutinn.serabutinnn.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.databinding.ItemsVerticalBinding
import com.serabutinn.serabutinnn.ui.customerpage.DetailJobCustomerActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class HistoryCustomerAdapter(private val id: UserModel) : ListAdapter<DataJobsCustomer, HistoryCustomerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val originalList = mutableListOf<DataJobsCustomer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, id)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsVerticalBinding, private val id: UserModel) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: DataJobsCustomer) {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobCustomerActivity::class.java)
                intent.putExtra(DetailJobCustomerActivity.ID, data.jobId.toString())
                binding.root.context.startActivity(intent)
            }
            binding.tvJudul.text = data.title
            binding.tvDuit.text = formatToRupiah(data.cost.toString())
            binding.tvWaktu.text = "Deadline | ${data.deadline}"
            binding.tvStatus.text = data.status
            binding.lokasi.text = data.location
            if(data.image==null){
                Glide.with(binding.root)
                    .load(R.drawable.serabutinn_notext)
                    .centerInside()
                    .into(binding.imgJobs)
            }
            else{Glide.with(binding.root)
                .load(data.image)
                .centerInside()
                .into(binding.imgJobs)}
            if (data.mitraId == id.id && id.roleid == "2") {
                binding.takenbyyou.visibility = View.VISIBLE
            } else {
                binding.takenbyyou.visibility = View.GONE
            }
            when (data.status) {
                "Pending" -> {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FFDA44"))
                    binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                }
                "In Progress" -> {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FFA500"))
                    binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                }
                "Completed" -> {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ECFFEC"))
                    binding.tvStatus.setTextColor(Color.parseColor("#188018"))
                }
                "Canceled" -> {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FF0000"))
                    binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                }
            }
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
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataJobsCustomer>() {
            override fun areItemsTheSame(
                oldItem: DataJobsCustomer,
                newItem: DataJobsCustomer
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataJobsCustomer,
                newItem: DataJobsCustomer
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.title?.contains(query, ignoreCase = true) ?: false }
        }
        submitList(filteredList)
    }

    fun setData(list: List<DataJobsCustomer>) {
        originalList.clear()
        originalList.addAll(list)
        submitList(list)
        Log.d("HistoryCustomerAdapter", "Original List Size: ${originalList.size}")
    }
}