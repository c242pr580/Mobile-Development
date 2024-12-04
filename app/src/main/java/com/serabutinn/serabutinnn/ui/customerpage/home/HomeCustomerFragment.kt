package com.serabutinn.serabutinnn.ui.customerpage.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.databinding.FragmentHomeCustomerBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeCustomerAdapter
import com.serabutinn.serabutinnn.ui.customerpage.AddJobsActivity
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class HomeCustomerFragment : Fragment() {
    private var _binding: FragmentHomeCustomerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeCustomerViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    companion object;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCustomerBinding.inflate(inflater,container,false)
        val view = binding.root

        binding.ibAdd.setOnClickListener{
            val intent = Intent(requireContext(), AddJobsActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.findJobs(user)
            binding.tvHiNama.text="Hi, ${user.name}"
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            showError(message)
        }
        viewModel.data.observe(viewLifecycleOwner) { data ->
            setJobsData(data)
        }
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvJobs.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvJobs.addItemDecoration(itemDecoration)
    }
    private fun showLoading(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showError(message: String) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Terjadi Kesalahan")
            .setMessage(message)
            .setPositiveButton("Okay"){_,_ -> }
    }
    private fun setJobsData(consumerReviews: List<DataJobsCustomer>) {
        viewModel.getSession().observe(viewLifecycleOwner){
            val adapter = HomeCustomerAdapter(it)
            adapter.submitList(consumerReviews)
            binding.rvJobs.adapter = adapter
        }

    }
}