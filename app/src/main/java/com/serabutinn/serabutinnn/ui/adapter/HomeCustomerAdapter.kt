package com.serabutinn.serabutinnn.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeCustomerAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.serabutinn.serabutinnn.ui.customerpage.DetailJobCustomerActivity

class HomeCustomerAdapter : ListAdapter<DataJobsCustomer, HomeCustomerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataJobsCustomer) {
            binding.cardView.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobCustomerActivity::class.java)
                intent.putExtra("id", data.jobId)
                binding.root.context.startActivity(intent)
            }
            binding.tvStatus.text = data.status
            if(data.status == "Pending"){

                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#F0FF0101"))
            }
            binding.tvDuit.text ="Rp. "+ data.cost
            binding.tvWaktu.text = data.deadline
            binding.tvJudul.text = data.title
            Log.e("gambar",data.image.toString())
            Glide.with(binding.root)
                .load(data.image)
                .centerInside()
                .into(binding.imgJobs)
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