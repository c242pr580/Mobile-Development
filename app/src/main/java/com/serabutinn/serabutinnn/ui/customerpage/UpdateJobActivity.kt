package com.serabutinn.serabutinnn.ui.customerpage

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityUpdateJobBinding
import com.serabutinn.serabutinnn.getImageUri
import com.serabutinn.serabutinnn.uriToFile
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateJobBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<UpdateJobViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val ID = "id"
        const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private var datepicked: String? = ""
    private lateinit var tvSelectedDate: TextView

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.btnGalery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.imageButton.setOnClickListener { showDatePicker() }
        tvSelectedDate = binding.tvSelectedDate

        viewModel.getSession().observe(this) {
            viewModel.getJobDetail(it.token, intent.getStringExtra(ID).toString())
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        binding.buttonUpload.setOnClickListener { uploadImage() }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.isSuccess.observe(this) {
            if (it) {
                Toast.makeText(this, "Job Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.data.observe(this) {
            if (it != null) {
                binding.txtInputTitle.setText(it.title)
                binding.txtInputDesc.setText(it.description)
                binding.txtInputPrice.setText(it.cost)
                binding.txtInputLocation.setText(it.location)
                binding.tvSelectedDate.text = "Deadline : ${it.deadline}"
                currentImageUri = Uri.parse(it.image.toString())
                showImage()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker() {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                datepicked = formattedDate
                tvSelectedDate.text = "Deadline : $datepicked"
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun startGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            legacyGalleryLauncher.launch(intent)
        }
    }

    private val legacyGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            currentImageUri = result.data?.data
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.ivJobs)
        }
    }

    private fun isRemoteUri(uri: Uri): Boolean {
        return uri.toString().startsWith("http") || uri.toString().startsWith("https")
    }

    private suspend fun downloadFile(remoteUri: Uri): File? {
        return withContext(Dispatchers.IO) { // Switch to IO thread for network operations
            try {
                val url = URL(remoteUri.toString())
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("DownloadFile", "Failed to connect: ${connection.responseCode}")
                    return@withContext null
                }

                val inputStream: InputStream = connection.inputStream
                val outputFile = File(cacheDir, "downloaded_image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(outputFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                outputFile // Return the downloaded file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun compressImage(file: File): File {
        var quality = 80
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressedFile = File(cacheDir, "compressed_${file.name}")
        do {
            compressedFile = File(cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(compressedFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.close()
            quality -= 10
        } while (compressedFile.length() > 1_000_000 && quality > 10)
        return compressedFile
    }

    private fun uploadImage() {
        showLoading(true)

        if (binding.txtInputDesc.text.isNullOrEmpty() ||
            binding.txtInputLocation.text.isNullOrEmpty() ||
            binding.txtInputPrice.text.isNullOrEmpty() ||
            binding.txtInputTitle.text.isNullOrEmpty()
        ) {
            showLoading(false)
            AlertDialog.Builder(this).apply {
                setTitle("Oops!")
                setMessage("Please fill all fields")
                setPositiveButton("OK") { _, _ -> }
            }.show()
            return
        }

        if (currentImageUri == null) {
            showLoading(false)
            AlertDialog.Builder(this).apply {
                setTitle("Oops!")
                setMessage("Please select an image before uploading.")
                setPositiveButton("OK") { _, _ -> }
            }.show()
            return
        }

        // Launch a coroutine for background work
        viewModel.getSession().observe(this) { user ->
            val token = user.token
            val id = intent.getStringExtra(ID)
            lifecycleScope.launch {
                val imageFile = if (isRemoteUri(currentImageUri!!)) {
                    val downloadedFile = downloadFile(currentImageUri!!)
                    if (downloadedFile != null) compressImage(downloadedFile) else null
                } else {
                    val tempFile = uriToFile(currentImageUri!!, this@UpdateJobActivity)
                    compressImage(tempFile)
                }

                if (imageFile != null) {
                    viewModel.checkTitle(
                        token,
                        binding.txtInputTitle.text.toString(),
                        binding.txtInputDesc.text.toString(),
                        datepicked.toString(),
                        binding.txtInputPrice.text.toString(),
                        binding.txtInputLocation.text.toString(),
                        imageFile,
                        id.toString()
                    )
                } else {
                    showLoading(false)
                    AlertDialog.Builder(this@UpdateJobActivity).apply {
                        setTitle("Error")
                        setMessage("Failed to process the image.")
                        setPositiveButton("OK") { _, _ -> }
                    }.show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
