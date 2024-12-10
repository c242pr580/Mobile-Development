package com.serabutinn.serabutinnn.ui.customerpage.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private var homeCustomerAdapter: HomeCustomerAdapter? = null
    private var originalList: List<DataJobsCustomer> = emptyList()
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeCustomerViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var backPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            user?.let {
                homeCustomerAdapter = HomeCustomerAdapter(it)
                binding.rvJobs.adapter = homeCustomerAdapter
                viewModel.findJobs(it)
                viewModel.getBiodata(it.token)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { jobs ->
            originalList = jobs
            val filteredList = originalList.filter { it.status == "In Progress" || it.status == "Pending" }
            homeCustomerAdapter?.submitFilteredList(filteredList)
        }

        viewModel.dataBio.observe(viewLifecycleOwner) { biodata ->
            showItems()
            if (biodata != null) {
                binding.tvHiNama.text = "Hi, ${biodata.name}"
                Glide.with(this)
                    .load(biodata.profilePicture)
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

        binding.apply {
            ibAdd.setOnClickListener {
                val intent = Intent(requireContext(), AddJobsActivity::class.java)
                startActivity(intent)
            }
            Profile.setOnClickListener {
                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
                bottomNavigationView.selectedItemId = R.id.navigation_notifications2
            }
            rvJobs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showItems() {
        binding.cview.visibility = View.VISIBLE
        binding.LLItems.visibility = View.VISIBLE
        binding.cardView2.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Terjadi Kesalahan")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleBackPress() {
        if (backPressedOnce) {
            activity?.finishAffinity()
        } else {
            backPressedOnce = true
            Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({ backPressedOnce = false }, 2000)
        }
    }
}
