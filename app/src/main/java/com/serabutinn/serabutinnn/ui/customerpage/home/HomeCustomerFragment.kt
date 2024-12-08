package com.serabutinn.serabutinnn.ui.customerpage.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.serabutinn.serabutinnn.R
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
        binding.Profile.setOnClickListener{
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
                bottomNavigationView.selectedItemId = R.id.navigation_notifications2

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.findJobs(user)
            viewModel.getBiodata(user.token)

        }
        viewModel.dataBio.observe(viewLifecycleOwner){
            if (it != null) {
                binding.tvHiNama.text="Hi, ${it.name}"
                Glide.with(this)
                    .load(it.profilePicture)
                    .circleCrop()
                    .into(binding.Profile)
            }
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