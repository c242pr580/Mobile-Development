package com.serabutinn.serabutinnn.ui.mitrapage.Profile

import android.Manifest
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
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
                // The file can now be sent directly
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
                Toast.makeText(requireContext(), "Berhasil Update", Toast.LENGTH_SHORT).show()
                viewModel.getSession().observe(viewLifecycleOwner) { user ->
                    viewModel.getBiodata(user.token)
                }
            }
        }
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

        viewModel.data.observe(viewLifecycleOwner) {
            Log.e("Data", it.toString())
            if (it != null) {
                binding.textView12.text = it.name
                binding.textView13.text = it.username
            }
            Glide.with(this)
                .load(it?.profilePicture)
                .circleCrop()
                .into(binding.imageView2)
            val listViews = binding.lvBio
            val item = listOf(
                ListItem("Nama", it?.name.toString()) { update("Nama", it?.name.toString()) },
                ListItem("Username", it?.username.toString()) {},
                ListItem("Email", it?.email.toString()) {},
                ListItem("No. Telepon", it?.phone.toString()) {
                    update(
                        "No. Telepon",
                        it?.phone.toString()
                    )
                },
                ListItem("Alamat", it?.location.toString()) {
                    update(
                        "Lokasi",
                        it?.location.toString()
                    )
                },
            )
            val adapter = CustomAdapter(requireContext(), R.layout.item_list, item)
            listViews.adapter = adapter
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

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            photoFile = this
        }
    }

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

        // Compress the image to JPEG format and reduce quality to ensure it stays under 1MB
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        // Verify the size is under 1MB, reduce quality further if needed
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

    private fun restartFragment() {
        val currentFragment = childFragmentManager.findFragmentById(R.id.fragmentContainer)
        currentFragment?.let {
            val fragmentTransaction = childFragmentManager.beginTransaction()

            // Clear the fragment from the back stack (if it exists)
            childFragmentManager.popBackStack(it::class.java.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            // Replace with a new instance
            fragmentTransaction.replace(R.id.fragmentContainer, it::class.java.newInstance())
            fragmentTransaction.addToBackStack(it::class.java.name) // Optionally add the new one to the back stack
            fragmentTransaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}