package com.serabutinn.serabutinnn.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.serabutinn.serabutinnn.databinding.ActivityFaceCameraBinding
import com.serabutinn.serabutinnn.getImageUri
import com.serabutinn.serabutinnn.ui.customerpage.HomeCustomerActivity
import com.serabutinn.serabutinnn.uriToFile
import com.serabutinn.serabutinnn.viewmodel.FaceCameraViewModel
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileOutputStream

class FaceCameraActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null

    companion object {
        const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private val viewModel: FaceCameraViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFaceCameraBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        viewModel.isSuccess.observe(this) {
            if (it) {
                Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeCustomerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        }
        val captureButton = binding.captureButton

        captureButton.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun showImagePickerDialog() {
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

    private fun showImage() {
        currentImageUri?.let { uri ->
            val file: File = uriToFile(uri, this)
            val compressedFile = compressImage(file)

            val multipartBody: MultipartBody.Part?
            val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaType())
            multipartBody = requestImageFile.let {
                MultipartBody.Part.createFormData(
                    "verification_image", compressedFile.name, it
                )
            }
            viewModel.getSession().observe(this) { user ->
                viewModel.uploadFace(user.token, multipartBody)
            }
        }
    }
}