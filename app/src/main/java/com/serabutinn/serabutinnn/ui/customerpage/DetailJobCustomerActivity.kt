package com.serabutinn.serabutinnn.ui.customerpage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobCustomerBinding
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date

class DetailJobCustomerActivity : AppCompatActivity() {
    companion object {
        const val ID = "id"
    }
    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_CAMERA = 101
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private val viewModel by viewModels<DetailJobCustomerViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailJobCustomerBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailJobCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        viewModel.data.observe(this) {
            val data = it
            binding.judulJob.text = data?.title
            binding.description.text = "Deskripsi : \n" + data?.description
            binding.location.text = "Lokasi : " + data?.location
            if (data != null) {
                binding.biaya.text = "Cost : " + data.cost?.let { it1 -> formatToRupiah(it1) }
            }
            binding.tvStatus.text = data?.status
            binding.deadline.text = "Deadline : " + data?.deadline
            Glide.with(this)
                .load(data?.image)
                .centerInside()
                .into(binding.imgJob)
            if (data != null) {
                if (data.image == null) {
                    binding.imgJob.visibility = View.GONE
                }
                when (data.status) {
                    "Pending" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ffde21"))
                        binding.tvStatus.setTextColor(Color.parseColor("#000000"))
                        binding.btnCompleted.visibility = View.GONE
                    }
                    "In Progress" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#5ce65c"))
                        binding.tvStatus.setTextColor(Color.parseColor("#0f4d0f"))
                        binding.btnCompleted.visibility = View.VISIBLE
                        binding.btnUpdate.visibility = View.GONE
                        binding.btnHapus.visibility = View.GONE
                    }
                    "Completed" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#B2BEB5"))
                        binding.tvStatus.setTextColor(Color.parseColor("#36454F"))
                        binding.btnCompleted.visibility = View.GONE
                        binding.btnUpdate.visibility = View.GONE
                        binding.btnHapus.visibility = View.GONE
                    }
                }

            }
        }
        binding.btnHapus.setOnClickListener {
            viewModel.getSession().observe(this) {
                viewModel.deleteJob(id.toString(), it.token)
                finish()
            }
        }

        viewModel.getSession().observe(this) {
            viewModel.getJobDetailCust(it.token, id.toString())
        }

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateJobActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
        binding.btnCompleted.setOnClickListener {
            if (checkAndRequestPermissions()) {
                openCamera()
            }
            viewModel.isVerified.observe(this) {
                if (!it) {
                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }
            }

        }

        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@DetailJobCustomerActivity, HomeCustomerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun formatToRupiah(number: String): String {
        val amount = number.toLongOrNull() ?: 0L
        val decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val decimalFormat = DecimalFormat("Rp #,###", decimalFormatSymbols)
        return decimalFormat.format(amount)
    }
    private fun checkAndRequestPermissions(): Boolean {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        val permissionGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION)
        }
        return permissionGranted
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir).apply {
            // Save the file path for further use
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            sendFile(photoFile)
        }
    }

    private fun sendFile(file: File) {
        viewModel.getSession().observe(this){
            viewModel.uploadFace(it.token,file)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }
}