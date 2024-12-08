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
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeCustomerAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.serabutinn.serabutinnn.ui.customerpage.DetailJobCustomerActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class HomeCustomerAdapter(private val id: UserModel) :
    ListAdapter<DataJobsCustomer, HomeCustomerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position),id)
    }

    class MyViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataJobsCustomer,id: UserModel) {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobCustomerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("id", data.jobId)
                binding.root.context.startActivity(intent)
            }
            binding.tvStatus.text = data.status
            if (data.status == "Pending") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#F0FF0101"))
            }
            binding.tvDuit.text = data.cost?.let { formatToRupiah(it) }
            binding.tvWaktu.text = "Deadline | ${data.deadline}"
            binding.tvJudul.text = data.title
            binding.lokasi.text = data.location
            Glide.with(binding.root)
                .load(data.image)
                .centerInside()
                .into(binding.imgJobs)
            if(data.mitraId == id.id && id.roleid=="2" ){
                binding.takenbyyou.visibility= View.VISIBLE
            }else{
                binding.takenbyyou.visibility=View.GONE
            }
            if (data.status == "Pending") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ffde21"))
                binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (data.status == "In Progress") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#5ce65c"))
                binding.tvStatus.setTextColor(Color.parseColor("#0f4d0f"))
            } else if (data.status == "Completed") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ECFFEC"))
                binding.tvStatus.setTextColor(Color.parseColor("#188018"))
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
    }
}