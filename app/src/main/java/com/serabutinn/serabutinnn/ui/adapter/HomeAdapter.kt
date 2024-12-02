package com.serabutinn.serabutinnn.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.data.api.response.HomeResponse
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeAdapter.MyViewHolder.Companion.DIFF_CALLBACK

class HomeAdapter : ListAdapter<DataAllJobs, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataAllJobs) {
            binding.tvJudul.text = data.title
            Glide.with(binding.root)
                .load(data.image)
                .centerCrop()
                .into(binding.imgJobs)
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
    }
}