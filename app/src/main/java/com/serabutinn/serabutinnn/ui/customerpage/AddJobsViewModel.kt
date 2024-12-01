package com.serabutinn.serabutinnn.ui.customerpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.CreateJobsResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddJobsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addJobs(token: String, imageFile: File?, description: String, title: String, deadline: String, price: String, location: String){
        var multipartBody: MultipartBody.Part? = null
        if (imageFile != null) {val requestImageFile = imageFile?.asRequestBody("image/jpeg".toMediaType())
            multipartBody = requestImageFile?.let {
                MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    it
                )
            }}

        val client = ApiClient.getApiService().createJob(token="Bearer $token", image = multipartBody, description = description, title = title, deadline = deadline, price = price.toInt(), location = location)
        client.enqueue(object : Callback<CreateJobsResponse> {
            override fun onResponse(
                call: Call<CreateJobsResponse>,
                response: Response<CreateJobsResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("success", response.body()?.message.toString())
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = true
                }
                else {Log.e("error", response.body()?.message.toString())
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<CreateJobsResponse>, t: Throwable) {
                _message.value = t.message.toString()
                _isSuccess.value = false
            }
        })
    }

    fun getSession() = repository.getSession().asLiveData()
}