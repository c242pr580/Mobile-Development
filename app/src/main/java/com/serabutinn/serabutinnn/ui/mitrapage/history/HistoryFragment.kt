package com.serabutinn.serabutinnn.ui.mitrapage.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.data.api.response.DataJobsMitra
import com.serabutinn.serabutinnn.databinding.FragmentHistoryBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.ui.adapter.HistoryAdapter
import com.serabutinn.serabutinnn.ui.adapter.HistoryCustomerAdapter
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class HistoryFragment : Fragment() {
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        lightStatusBar(requireActivity().window)
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observe LiveData from the ViewModel
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.roleid == "1") {
                viewModel.getCustomerJobs(user.token)
            } else {
                viewModel.getMitraJobs(user.token)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            showError(message)
        }
        viewModel.data.observe(viewLifecycleOwner) { data -> setJobsData(data) }
        viewModel.dataCustomer.observe(viewLifecycleOwner) { data -> setJobsDataCustomer(data) }
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvJobs.layoutManager = layoutManager
    }

    private fun setJobsData(consumerReviews: List<DataJobsMitra?>?) {
        viewModel.getSession().observe(viewLifecycleOwner){user->
            val adapter = HistoryAdapter(user)
            adapter.submitList(consumerReviews)
            adapter.setData(consumerReviews as List<DataJobsMitra>)
            binding.rvJobs.adapter = adapter
            binding.textInputEditText.addTextChangedListener{ text ->
                adapter.filter(text.toString())
            }
        }

    }
    private fun setJobsDataCustomer(consumerReviews: List<DataJobsCustomer?>?) {
        viewModel.getSession().observe(viewLifecycleOwner){
            val adapter = HistoryCustomerAdapter(it)
            adapter.submitList(consumerReviews)
            adapter.setData(consumerReviews as List<DataJobsCustomer>)
            binding.rvJobs.adapter = adapter
            binding.textInputEditText.addTextChangedListener{ text ->
                adapter.filter(text.toString())
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Terjadi Kesalahan")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
