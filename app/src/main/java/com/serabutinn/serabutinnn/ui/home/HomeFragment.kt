package com.serabutinn.serabutinnn.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.databinding.FragmentHomeBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeAdapter
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observe LiveData from the ViewModel
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            showError(message)
        }
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvJobs.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvJobs.addItemDecoration(itemDecoration)
    }
    private fun setJobsData(consumerReviews: List<DataAllJobs>) {
        val adapter = HomeAdapter()
        adapter.submitList(consumerReviews)
        binding.rvJobs.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Terjadi Kesalahan")
            .setMessage(message)
            .setPositiveButton("Okay"){_,_ -> }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}