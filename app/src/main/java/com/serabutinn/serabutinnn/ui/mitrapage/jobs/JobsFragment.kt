package com.serabutinn.serabutinnn.ui.mitrapage.jobs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.serabutinn.serabutinnn.databinding.FragmentJobsBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.ui.adapter.JobsPendingAdapter
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class JobsFragment : Fragment() {
    private val viewModel by viewModels<JobsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var jobsPendingAdapter: JobsPendingAdapter? = null
    private var _binding: FragmentJobsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        lightStatusBar(requireActivity().window)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            jobsPendingAdapter = JobsPendingAdapter(user)
            viewModel.findJobs(user)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            showError(message)
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            val pendingJobs = data.filter { it.status == "Pending" }
            if (pendingJobs.isEmpty()) {
                binding.nojob2.visibility = View.VISIBLE
            } else {
                binding.nojob2.visibility = View.GONE
                jobsPendingAdapter?.setData(pendingJobs)
            }
            binding.rvJobs.adapter = jobsPendingAdapter
            setupRecyclerView()
        }
        binding.textInputEditText.addTextChangedListener{
            val query = binding.textInputEditText.text.toString()
            jobsPendingAdapter?.filter(query)
        }
    }

    private fun setupRecyclerView() {
        binding.rvJobs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
}
