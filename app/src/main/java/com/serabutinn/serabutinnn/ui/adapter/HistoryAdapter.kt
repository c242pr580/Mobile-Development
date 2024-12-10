package com.serabutinn.serabutinnn.ui.adapter

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
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.DataJobsMitra
import com.serabutinn.serabutinnn.databinding.ItemsHorizontalBinding
import com.serabutinn.serabutinnn.ui.DetailJobActivity
import com.serabutinn.serabutinnn.ui.adapter.HistoryAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class HistoryAdapter(private val id:UserModel) : ListAdapter<DataJobsMitra, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val originalList = mutableListOf<DataJobsMitra>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,id)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsHorizontalBinding,private val id:UserModel) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataJobsMitra) {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobActivity::class.java)
                intent.putExtra(DetailJobActivity.ID, data.jobId.toString())
                binding.root.context.startActivity(intent)
            }
            binding.tvJudul.text = data.title
            binding.tvDuit.text = formatToRupiah(data.cost.toString())
            binding.tvWaktu.text = data.deadline
            binding.tvStatus.text = data.status
            Glide.with(binding.root)
                .load(data.image)
                .centerCrop()
                .into(binding.imgJobs)
            if(data.mitraId == id.id && id.roleid=="2" ){
                binding.takenbyyou.visibility= View.VISIBLE
            }else{binding.takenbyyou.visibility= View.GONE}
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
                "Canceled" ->{
                    binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FF0000"))
                    binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF")) }
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
        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataJobsMitra>() {
                override fun areItemsTheSame(
                    oldItem: DataJobsMitra,
                    newItem: DataJobsMitra
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: DataJobsMitra,
                    newItem: DataJobsMitra
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalList // No filtering, return the original list
        } else {
            originalList.filter { it.title?.contains(query, ignoreCase = true) ?: false }
        }
        submitList(filteredList)  // Update the list using submitList
    }
    fun setData(list: List<DataJobsMitra>) {
        originalList.clear()
        originalList.addAll(list)
        submitList(list)
        Log.d("HistoryCustomerAdapter", "Original List Size: ${originalList.size}")
    // Display the original data initially
    }
}