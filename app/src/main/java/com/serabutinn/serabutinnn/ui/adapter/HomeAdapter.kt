package com.serabutinn.serabutinnn.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.DetailJobActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class HomeAdapter(private val id: UserModel) : ListAdapter<DataAllJobs, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    // Store the original unaltered list
    private val originalList = mutableListOf<DataAllJobs>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, id)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsBinding, private val id: UserModel) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataAllJobs) {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobActivity::class.java)
                intent.putExtra(DetailJobActivity.ID, data.jobId.toString())
                binding.root.context.startActivity(intent)
            }
            binding.tvJudul.text = data.title
            binding.tvDuit.text = formatToRupiah(data.cost.toString())
            binding.tvWaktu.text = data.deadline
            binding.tvStatus.text = data.status
            binding.lokasi.text = data.location
            Glide.with(binding.root)
                .load(data.image)
                .centerCrop()
                .into(binding.imgJobs)

            if (data.mitraId == id.id && id.roleid == "2") {
                binding.takenbyyou.visibility = View.VISIBLE
            } else {
                binding.takenbyyou.visibility = View.GONE
            }

            when (data.status) {
                "Pending" -> {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ffde21"))
                    binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                }
                "In Progress" -> {
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#5ce65c"))
                    binding.tvStatus.setTextColor(Color.parseColor("#0f4d0f"))
                }
                "Completed" -> {
                    binding.root.visibility = View.GONE
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#B2BEB5"))
                    binding.tvStatus.setTextColor(Color.parseColor("#36454F"))
                }
                "Canceled" -> {
                    binding.root.visibility = View.GONE
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataAllJobs>() {
            override fun areItemsTheSame(
                oldItem: DataAllJobs,
                newItem: DataAllJobs
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataAllJobs,
                newItem: DataAllJobs
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    // Filter function
    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalList // Use the original list when query is empty
        } else {
            originalList.filter { it.title?.contains(query, ignoreCase = true) ?: false }
        }
        submitList(filteredList)  // Update the list using submitList
    }

    // Set data method to store original list
    fun setData(list: List<DataAllJobs>) {
        originalList.clear() // Clear the original list
        originalList.addAll(list) // Add all items to the original list
        submitList(list)  // Display the original data initially
    }
}