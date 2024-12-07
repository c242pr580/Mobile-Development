package com.serabutinn.serabutinnn.ui.mitrapage.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.data.ListItem
import com.serabutinn.serabutinnn.databinding.FragmentProfileBinding
import com.serabutinn.serabutinnn.ui.UpdateBioActivity
import com.serabutinn.serabutinnn.ui.adapter.CustomAdapter
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
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.getBiodata(user.token)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), MainActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.data.observe(viewLifecycleOwner){
            Log.e("Data",it.toString())
            if (it != null) {
                binding.textView12.text = it.name
                binding.textView13.text = it.username
            }
            Glide.with(this)
                .load(it?.profilePicture)
                .circleCrop()
                .into(binding.imageView2)
            val listViews= binding.lvBio
            val item= listOf(
                ListItem("Nama",it?.name.toString()) { update("Nama", it?.name.toString()) },
                ListItem("Username",it?.username.toString()){},
                ListItem("Email",it?.email.toString()){},
                ListItem("No. Telepon",it?.phone.toString()){ update("No. Telepon", it?.phone.toString()) },
                ListItem("Alamat",it?.location.toString()){ update("Lokasi", it?.location.toString()) },
            )
            val adapter= CustomAdapter(requireContext(), R.layout.item_list,item)
            listViews.adapter=adapter
        }

    }
    private fun update(title:String,subtitle:String){
        val intent = Intent(requireContext(), UpdateBioActivity::class.java)
        intent.putExtra(UpdateBioActivity.TITLE, title)
        intent.putExtra(UpdateBioActivity.SUBTITLE, subtitle)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}