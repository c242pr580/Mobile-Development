package com.serabutinn.serabutinnn.ui.mitrapage.home

import android.annotation.SuppressLint
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
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.databinding.FragmentHomeBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeAdapter
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var backPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.Profile.setOnClickListener{
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavigationView.selectedItemId = R.id.navigation_notifications
        }
        return root

    }
    fun handleBackPress() {
        if (backPressedOnce) {
            activity?.finishAffinity() // Exit the app
        } else {
            backPressedOnce = true
            Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()

            // Reset the flag after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({ backPressedOnce = false }, 2000)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observe LiveData from the ViewModel
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
            viewModel.getSession().observe(viewLifecycleOwner) { user ->
                val pending: MutableList<DataAllJobs> = data.filter { it.status == "Pending" }.toMutableList()
                val inProgress: MutableList<DataAllJobs> =
                    data.filter { it.status == "In Progress" && it.mitraId != user.id.trim() }.toMutableList()
                val completed: MutableList<DataAllJobs> = data.filter { it.status == "Completed" }.toMutableList()
                val takenByMe: MutableList<DataAllJobs> =
                    data.filter { it.mitraId == user.id.trim() && it.status == "In Progress" }.toMutableList()
                val dataAllJobs: MutableList<DataAllJobs> = mutableListOf()
                if (takenByMe.isNotEmpty()) {takenByMe.map {dataAllJobs.add(it)} }
                if (pending.isNotEmpty()) {pending.map { dataAllJobs.add(it) }}
                if (inProgress.isNotEmpty()) {inProgress.map { dataAllJobs.add(it) }}
                if (completed.isNotEmpty()) { completed.map {dataAllJobs.add(it)} }

                setJobsData(dataAllJobs)
            }
        }
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvJobs.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvJobs.addItemDecoration(itemDecoration)
    }

    private fun setJobsData(consumerReviews: List<DataAllJobs>) {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            val adapter = HomeAdapter(user)
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