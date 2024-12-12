package com.serabutinn.serabutinnn.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.serabutinn.serabutinnn.databinding.ActivityUpdateBioBinding
import com.serabutinn.serabutinnn.lightStatusBar
import com.serabutinn.serabutinnn.viewmodel.ViewModelFactory

class UpdateBioActivity : AppCompatActivity() {
    companion object {
        const val TITLE = "title"
        const val SUBTITLE = "subtitle"
    }
    private val viewModel by viewModels<UpdateBioViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityUpdateBioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(window)
        hideLoading()
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val subtitle = intent.getStringExtra("subtitle")
        val title = intent.getStringExtra("title")
        binding.textView9.text=title
        binding.inputan.hint=title
        binding.inputan.setHintTextColor(Color.BLACK)
        binding.inputan.text = Editable.Factory.getInstance().newEditable(subtitle)
        binding.buttonconfirm.setOnClickListener {
            showLoading()
            viewModel.getSession().observe(this) { user ->
                if(title=="Name"){
                    viewModel.updateBio(user.token,binding.inputan.text.toString(),null,null)
                }
                else if(title=="Phone"){
                    viewModel.updateBio(user.token,null,binding.inputan.text.toString(),null)
                }
                else if(title=="Location"){
                    viewModel.updateBio(user.token,null,null,binding.inputan.text.toString())
                }
                else if(title=="Nama Bisnis"){
                    viewModel.updateMitra(user.token,binding.inputan.text.toString(),"")
                }
                else if(title=="Alamat Bisnis"){
                    viewModel.updateMitra(user.token,"",binding.inputan.text.toString())
                }
            }
        }
        viewModel.isSuccess.observe(this){
            if(it){
                hideLoading()
                showToast("Berhasil Mengupdate Biodata")
                finish()
            }
        }
        viewModel.message.observe(this){
            hideLoading()
            showToast(it)
        }
    }

    private fun showLoading() {
        binding.progressBar4.visibility = android.view.View.VISIBLE
    }
    private fun hideLoading() {
        binding.progressBar4.visibility = android.view.View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}