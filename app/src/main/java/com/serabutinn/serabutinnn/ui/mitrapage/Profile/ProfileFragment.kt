package com.serabutinn.serabutinnn.ui.mitrapage.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.FragmentProfileBinding
import com.serabutinn.serabutinnn.ui.auth.MainActivity2
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), MainActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }

        viewModel.data.observe(viewLifecycleOwner){
            binding.textView12.text = it.data?.name
            Glide.with(this)
                .load(it.data?.profilePicture)
                .into(binding.imageView2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}