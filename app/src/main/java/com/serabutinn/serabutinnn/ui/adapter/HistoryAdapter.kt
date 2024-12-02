package com.serabutinn.serabutinnn.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.data.api.response.DataJobsMitra
import com.serabutinn.serabutinnn.data.api.response.ListJobsMitraResponse
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.DetailJobActivity
import com.serabutinn.serabutinnn.ui.adapter.HistoryAdapter.MyViewHolder.Companion.DIFF_CALLBACK

class HistoryAdapter : ListAdapter<DataJobsMitra, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataJobsMitra) {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobActivity::class.java)
                intent.putExtra("id", data.jobId)
                binding.root.context.startActivity(intent)
            }
            binding.tvJudul.text = data.title
            Glide.with(binding.root)
                .load(data.image)
                .centerCrop()
                .into(binding.imgJobs)
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
}