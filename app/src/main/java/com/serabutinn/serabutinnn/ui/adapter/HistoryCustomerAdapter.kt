package com.serabutinn.serabutinnn.ui.adapter

import android.annotation.SuppressLint
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
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.databinding.ItemsBinding
import com.serabutinn.serabutinnn.ui.DetailJobActivity
import com.serabutinn.serabutinnn.ui.adapter.HistoryCustomerAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.serabutinn.serabutinnn.ui.customerpage.DetailJobCustomerActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class HistoryCustomerAdapter(private val id: UserModel) : ListAdapter<DataJobsCustomer, HistoryCustomerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,id)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(private val binding: ItemsBinding,private val id: UserModel) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: DataJobsCustomer) {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailJobActivity::class.java)
                intent.putExtra(DetailJobActivity.ID, data.jobId.toString())
                binding.root.context.startActivity(intent)
            }
            binding.tvJudul.text = data.title
            binding.tvDuit.text = formatToRupiah(data.cost.toString())
            binding.tvWaktu.text = "Deadline | ${data.deadline}"
            binding.tvStatus.text = data.status
            Glide.with(binding.root)
                .load(data.image)
                .centerCrop()
                .into(binding.imgJobs)
            if(data.mitraId == id.id && id.roleid=="2" ){
                binding.takenbyyou.visibility= View.VISIBLE
            }else{binding.takenbyyou.visibility= View.GONE}
            if (data.status == "Pending") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ffde21"))
                binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
            } else if (data.status == "In Progress") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#5ce65c"))
                binding.tvStatus.setTextColor(Color.parseColor("#0f4d0f"))
            } else if (data.status == "Completed") {
                binding.cvStatus.setCardBackgroundColor(Color.parseColor("#B2BEB5"))
                binding.tvStatus.setTextColor(Color.parseColor("#36454F"))
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