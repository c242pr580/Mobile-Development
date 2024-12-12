package com.serabutinn.serabutinnn.ui.mitrapage.Profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.data.ListItem
import com.serabutinn.serabutinnn.databinding.FragmentProfileBinding
import com.serabutinn.serabutinnn.ui.UpdateBioActivity
import com.serabutinn.serabutinnn.ui.adapter.CustomAdapter
import com.serabutinn.serabutinnn.ui.auth.MainActivity2
import com.serabutinn.serabutinnn.ui.customerpage.AddJobsActivity.Companion.REQUIRED_PERMISSION
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

class ProfileFragment : Fragment() {
    private lateinit var photoFile: File
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                sendFile(photoFile)
            } else {
                Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val copiedFile = copyUriToFile(it)
                // The copied file can now be sent
                sendFile(copiedFile)
            }
        }
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.imageView2.setOnClickListener {
            showImagePickerDialog()
        }
        if (!allPermissionsGranted()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        viewModel.isSuccess.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(), "Updated Biodata Successfully", Toast.LENGTH_SHORT).show()
                viewModel.getSession().observe(viewLifecycleOwner) { user ->
                    viewModel.getBiodata(user.token)
                }
            }
        }
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.getBiodata(user.token)
            if(user.roleid=="2"){
                viewModel.getDetailMitra(user.token, user.id)
            }
        }
        val ratingBar2 = binding.ratingBar2
        ratingBar2.isClickable = false
        ratingBar2.isFocusable = false
        ratingBar2.isFocusableInTouchMode = false
        ratingBar2.setOnTouchListener { _, _ -> true }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        binding.ratingBar2.visibility=View.GONE
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), MainActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.data.observe(viewLifecycleOwner) {
                showItems()
                Log.e("Data", it.toString())
                if (it != null) {
                    binding.tvname.text = it.name
                    binding.tvemail.text = it.email
                    binding.tvphone.text = it.phone
                }
                Glide.with(this)
                    .load(it?.profilePicture)
                    .circleCrop()
                    .into(binding.imageView2)
                val listViews = binding.lvBio
                val item = listOf(
                    ListItem("Name", it?.name.toString()) { update("Name", it?.name.toString()) },
                    ListItem("Username", it?.username.toString()) {},
                    ListItem("Email", it?.email.toString()) {},
                    ListItem("Phone", it?.phone.toString()) {
                        update(
                            "Phone",
                            it?.phone.toString()
                        )
                    },
                    ListItem("Address", it?.location.toString()) {
                        update(
                            "Location",
                            it?.location.toString()
                        )
                    },
                )
                viewModel.getSession().observe(viewLifecycleOwner){user ->
                    if(user.roleid=="2"){
                        viewModel.dataMitra.observe(viewLifecycleOwner){mitra->
                            binding.ratingBar2.visibility=View.VISIBLE
                        binding.ratingBar2.rating = mitra?.rating?.toFloat() ?: 0f
                        val item2 = listOf(
                            ListItem("Name", it?.name.toString()) { update("Name", it?.name.toString()) },
                            ListItem("Username", it?.username.toString()) {},
                            ListItem("Email", it?.email.toString()) {},
                            ListItem("Phone", it?.phone.toString()) {
                                update(
                                    "Phone",
                                    it?.phone.toString()
                                )
                            },
                            ListItem("Address", it?.location.toString()) {
                                update(
                                    "Location",
                                    it?.location.toString()
                                )
                            },
                            ListItem("Business Name", mitra?.businessName.toString()) {
                                update(
                                    "Nama Bisnis",
                                    if(mitra?.businessName.isNullOrEmpty()){
                                        ""
                                    }else{mitra?.businessName.toString()}
                                )},
                            ListItem("Business Address", mitra?.businessAddress.toString()) {
                                update(
                                    "Alamat Bisnis",
                                    if(mitra?.businessAddress.isNullOrEmpty()){
                                        ""
                                    }else{mitra?.businessAddress.toString()}

                                )
                            }
                        )
                        val adapter = CustomAdapter(requireContext(), R.layout.item_list, item2)
                        listViews.adapter = adapter
                        }
                    }
                    else{
                        val adapter = CustomAdapter(requireContext(), R.layout.item_list, item)
                        listViews.adapter = adapter
                    }
                }




        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    private fun update(title: String, subtitle: String) {
        val intent = Intent(requireContext(), UpdateBioActivity::class.java)
        intent.putExtra(UpdateBioActivity.TITLE, title)
        intent.putExtra(UpdateBioActivity.SUBTITLE, subtitle)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showItems(){
        binding.cardView4.visibility=View.VISIBLE
        binding.tvakun.visibility=View.VISIBLE
        binding.buttonLogout.visibility=View.VISIBLE
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> dispatchTakePictureIntent() // Camera
                1 -> openGallery()              // Gallery
            }
        }
        builder.show()
    }

    private fun dispatchTakePictureIntent() {
        val photoFile = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(
                requireContext(),
                "Error occurred while creating the file",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        photoFile?.also {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                it
            )
            takePictureLauncher.launch(photoUri)
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            photoFile = this
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun copyUriToFile(uri: Uri): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        val file = File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir)

        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to copy file", Toast.LENGTH_SHORT).show()
        }

        return file
    }
    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(requireContext().cacheDir, "compressed_${file.name}")
        val outputStream = FileOutputStream(compressedFile)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        while (compressedFile.length() > 1_000_000) {
            val reducedQualityBitmap = BitmapFactory.decodeFile(compressedFile.path)
            compressedFile.delete()
            val reducedStream = FileOutputStream(compressedFile)
            reducedQualityBitmap.compress(Bitmap.CompressFormat.JPEG, 50, reducedStream)
            reducedStream.flush()
            reducedStream.close()
        }

        return compressedFile
    }

    private fun sendFile(file: File) {
        val compressedFile = compressImage(file)
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            viewModel.updateBio(user.token, compressedFile, null, null, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}