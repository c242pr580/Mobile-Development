package com.serabutinn.serabutinnn.ui.mitrapage.Profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.DataBio
import com.serabutinn.serabutinnn.data.api.response.UpdateBioResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<DataBio?>()
    val data: LiveData<DataBio?> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getBiodata(token:String){
        _isLoading.value = true
        val client = ApiClient.getApiService().getBiodata("Bearer $token")
        client.enqueue(object : Callback<BiodataResponse> {
            override fun onResponse(
                call: Call<BiodataResponse>,
                response: Response<BiodataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.e("Data", response.body()?.data.toString())
                    _data.value = response.body()?.data
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<BiodataResponse>, t: Throwable) {
                Log.e("error2", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })

    }
    fun updateBio(token: String, imageFile: File?, name: String?, phone: String?, location: String?) {
        var multipartBody: MultipartBody.Part? = null
        if (imageFile != null) {
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            multipartBody = requestImageFile?.let {
                MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    it
                )
            }
        }
        val client = ApiClient.getApiService().updateBiodata("Bearer $token",
            name?.toRequestBody("text/plain".toMediaTypeOrNull()),
            phone?.toRequestBody("text/plain".toMediaTypeOrNull()),
            location?.toRequestBody("text/plain".toMediaTypeOrNull()),
            multipartBody)
        client.enqueue(object : Callback<UpdateBioResponse> {
            override fun onResponse(
                call: Call<UpdateBioResponse>,
                response: Response<UpdateBioResponse>
            ) {
                if (response.isSuccessful) {
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = true
                } else {
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<UpdateBioResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _message.value = t.message.toString()
                _isSuccess.value = false
            }
        })

    }
}