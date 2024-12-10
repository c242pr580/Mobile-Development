package com.serabutinn.serabutinnn.ui.customerpage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.serabutinn.serabutinnn.databinding.ActivityDetailJobCustomerBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date

class DetailJobCustomerActivity : AppCompatActivity() {
    companion object {
        const val ID = "id"
    }
    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var photoFile: File
    private val viewModel by viewModels<DetailJobCustomerViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailJobCustomerBinding
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // The file can now be sent directly
                sendFile(photoFile)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailJobCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        val id = intent.getStringExtra("id")
        viewModel.data.observe(this) {
            val data = it
            binding.judulJob.text = data?.title
            binding.description.text = data?.description
            binding.location.text = data?.location
            if (data != null) {
                binding.biaya.text = data.cost?.let { it1 -> formatToRupiah(it1) }
            }
            binding.tvStatus.text = data?.status
            binding.deadline.text = "Deadline | " + data?.deadline
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
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FFDA44"))
                        binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.btnUpdate.visibility = View.VISIBLE
                        binding.btnHapus.visibility = View.VISIBLE
                        showItems()
                    }
                    "In Progress" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FFA500"))
                        binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.btnCompleted.visibility = View.VISIBLE
                        showItems()
                    }
                    "Completed" -> {
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#ECFFEC"))
                        binding.tvStatus.setTextColor(Color.parseColor("#188018"))
                        showItems()
                    }
                    "Canceled" ->{
                        binding.cvStatus.setCardBackgroundColor(Color.parseColor("#FF0000"))
                        binding.tvStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        showItems()
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
                dispatchTakePictureIntent()
            }
            viewModel.isVerified.observe(this) {
                if (!it) {
                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showItems(){
        binding.tvloc.visibility=View.VISIBLE
        binding.tvdesc.visibility=View.VISIBLE
        binding.imgJob.visibility=View.VISIBLE
        binding.vwLine.visibility=View.VISIBLE
        binding.cvStatus.visibility=View.VISIBLE
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
            Log.e("Permission", "Permission not granted")
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION)
        }
        return permissionGranted
    }

    private fun dispatchTakePictureIntent() {
        val photoFile = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(
                this,
                "Error occurred while creating the file",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        photoFile?.also {
            val photoUri = FileProvider.getUriForFile(
                this,
                "${this.packageName}.fileprovider",
                it
            )
            takePictureLauncher.launch(photoUri)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            photoFile = this
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

    private fun sendFile(file: File) {
        val compressedFile = compressImage(file)
        viewModel.getSession().observe(this){
            viewModel.uploadFace(it.token,compressedFile)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }
}