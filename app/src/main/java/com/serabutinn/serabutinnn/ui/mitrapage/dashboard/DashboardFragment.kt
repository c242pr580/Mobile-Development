package com.serabutinn.serabutinnn.ui.mitrapage.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.data.api.response.DataJobsMitra
import com.serabutinn.serabutinnn.databinding.FragmentDashboardBinding
import com.serabutinn.serabutinnn.ui.adapter.HistoryAdapter
import com.serabutinn.serabutinnn.ui.adapter.HistoryCustomerAdapter
import com.serabutinn.serabutinnn.ui.adapter.HomeAdapter
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class DashboardFragment : Fragment() {
    private val viewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvJobs.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvJobs.addItemDecoration(itemDecoration)

    }

    private fun setJobsData(consumerReviews: List<DataJobsMitra?>?) {
        viewModel.getSession().observe(viewLifecycleOwner){user->
            val adapter = HistoryAdapter(user)
            adapter.submitList(consumerReviews)
            binding.rvJobs.adapter = adapter
        }

    }
    private fun setJobsDataCustomer(consumerReviews: List<DataJobsCustomer?>?) {
        viewModel.getSession().observe(viewLifecycleOwner){
            val adapter = HistoryCustomerAdapter(it)
            adapter.submitList(consumerReviews)
            binding.rvJobs.adapter = adapter
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
