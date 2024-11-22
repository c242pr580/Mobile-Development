package com.serabutinn.serabutinnn.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serabutinn.serabutinnn.data.api.response.HomeResponse
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeAdapter.MyViewHolder.Companion.DIFF_CALLBACK

class HomeAdapter : ListAdapter<HomeResponse.Data, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HomeResponse.Data) {
        }
        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeResponse.Data>() {
                override fun areItemsTheSame(
                    oldItem: HomeResponse.Data,
                    newItem: HomeResponse.Data
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: HomeResponse.Data,
                    newItem: HomeResponse.Data
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}