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
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityUpdateJobBinding
import com.serabutinn.serabutinnn.getImageUri
import com.serabutinn.serabutinnn.uriToFile
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
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
        viewModel.getSession().observe(this){
            viewModel.getJobDetail(it.token,intent.getStringExtra(ID).toString())
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        binding.buttonUpload.setOnClickListener { uploadImage() }
        if (!allPermissionsGranted()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            requestPermissionLauncher.launch(AddJobsActivity.REQUIRED_PERMISSION)
        }
        viewModel.isSuccess.observe(this) {
            if (it) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeCustomerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        viewModel.isSuccess.observe(this) { it ->
            if (!it) {
                viewModel.message.observe(this) {
                    Log.e("Tes Activity", it)
                    AlertDialog.Builder(this).apply {
                        setTitle("Oops")
                        setMessage(it)
                        setPositiveButton("OK") { _, _ -> }
                    }
                }
            }
        }
        viewModel.data.observe(this) {
            if (it != null) {
                binding.txtInputTitle.setText(it.title)
                binding.txtInputDesc.setText(it.description)
                binding.txtInputPrice.setText(it.cost)
                binding.txtInputLocation.setText(it.location)
                binding.tvSelectedDate.text = "Deadline : ${it.deadline}"
                currentImageUri=Uri.parse(it.image.toString())
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
                datepicked = formattedDate.toString()
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
    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(cacheDir, "compressed_${file.name}")
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

    private fun uploadImage() {
        showLoading(true)
        if (binding.txtInputDesc.text.toString().isEmpty() ||
            binding.txtInputLocation.text.toString().isEmpty() ||
            binding.txtInputPrice.text.toString().isEmpty() ||
            binding.txtInputTitle.text.toString().isEmpty()
        ) {
            showLoading(false)
            AlertDialog.Builder(this).apply {
                setTitle("Oops!")
                setMessage("Please fill all fields")
                setPositiveButton("OK") { _, _ -> }
            }
            return
        } else {
            showLoading(true)
            viewModel.getSession().observe(this) { user ->
                val token = user.token
                val id = intent.getStringExtra(ID)
                currentImageUri?.let { uri ->
                    val imageFile =compressImage(uriToFile(uri, this))
                    val description = binding.txtInputDesc.text.toString()
                    viewModel.updateJob(
                        token,
                        binding.txtInputTitle.text.toString(),
                        description,
                        datepicked.toString(),
                        binding.txtInputPrice.text.toString(),
                        binding.txtInputLocation.text.toString(),
                        imageFile,
                        id.toString()
                    )}


            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
