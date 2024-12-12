package com.serabutinn.serabutinnn.ui.mitrapage.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.databinding.FragmentHomeBinding
import com.serabutinn.serabutinnn.ui.adapter.HomeAdapter
import com.serabutinn.serabutinnn.ui.adapter.HomePendingAdapter
import com.serabutinn.serabutinnn.ui.mitrapage.jobs.JobsFragment
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var homeAdapter: HomeAdapter? = null
    private var homePendingAdapter: HomePendingAdapter? = null
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
        binding.Profile.setOnClickListener {
            val intents =
                Intent(Intent.ACTION_DEFAULT, Uri.parse("https://serabutinn.com/transaction"))
            startActivity(intents)
//            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
//            bottomNavigationView.selectedItemId = R.id.navigation_notifications
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
            homeAdapter = HomeAdapter(user)
            homePendingAdapter = HomePendingAdapter(user)
            viewModel.findJobs(user)
            viewModel.getBiodata(user.token)
        }
        viewModel.dataBio.observe(viewLifecycleOwner) {
            showItems()
            if (it != null) {
                binding.tvHiNama.text = "Hi, ${it.name}"
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

        binding.ibAdd.setOnClickListener{
            replaceFragment(JobsFragment())
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data.isEmpty()) {
                binding.apply {
                    ivListKosong.visibility = View.VISIBLE
                    tvJobs.visibility = View.GONE
                    tvJobsIn.visibility = View.GONE
                }
            } else {
                binding.ivListKosong.visibility = View.GONE
            }
            val pendingJobs = data.filter { it.status == "Pending" }
            val inProgressJobs = data.filter { it.status == "In Progress" }

            homePendingAdapter?.submitFilteredList(pendingJobs)
            homeAdapter?.submitFilteredList(inProgressJobs)
            binding.apply {
                rvJobs.adapter = homePendingAdapter
                rvJobsIn.adapter = homeAdapter
            }
        }
        binding.apply {
            rvJobs.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvJobsIn.layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showItems() {
        binding.apply {
            cview.visibility = View.VISIBLE
            Llitems.visibility = View.VISIBLE
            cardView2.visibility = View.VISIBLE
            horizontalScrollViewHome2.visibility = View.VISIBLE
            tvPromo.visibility = View.VISIBLE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
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