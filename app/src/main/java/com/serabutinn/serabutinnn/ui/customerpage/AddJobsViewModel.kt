package com.serabutinn.serabutinnn.ui.customerpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.CreateJobsResponse
import com.serabutinn.serabutinnn.data.api.response.TitleCheckResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddJobsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _jobId = MutableLiveData<String>()
    val jobId: LiveData<String> = _jobId

    fun checkTitle(
        token: String,
        imageFile: File?,
        description: String,
        title: String,
        deadline: String,
        price: String,
        location: String
    ) {
        val client = ApiClient.getApiService().checkTitle(token = "Bearer $token", title = title)
        client.enqueue(object : Callback<TitleCheckResponse> {
            override fun onResponse(
                call: Call<TitleCheckResponse>,
                response: Response<TitleCheckResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("success", response.body()?.result.toString())
                    if (response.body()?.result == "Legal") {
                        addJobs(token, imageFile, description, title, deadline, price, location)
                    }else{
                        _isSuccess.value = false
                        Log.e("TES",response.body()?.message.toString())
                        _message.value = response.body()?.message.toString()
                    }
                } else {
                    _isSuccess.value = false
                    Log.e("error", response.body()?.result.toString())
                    _message.value = response.body()?.message.toString()
                }
            }

            override fun onFailure(call: Call<TitleCheckResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _message.value = t.message.toString()
                _isSuccess.value = false
            }
        })
    }

    fun addJobs(
        token: String,
        imageFile: File?,
        description: String,
        title: String,
        deadline: String,
        price: String,
        location: String
    ) {
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

        val client = ApiClient.getApiService().createJob(
            token = "Bearer $token",
            image = multipartBody,
            description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
            title = title.toRequestBody("text/plain".toMediaTypeOrNull()),
            deadline = deadline.toRequestBody("text/plain".toMediaTypeOrNull()),
            price = price.toRequestBody("text/plain".toMediaTypeOrNull()),
            location = location.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        client.enqueue(object : Callback<CreateJobsResponse> {
            override fun onResponse(
                call: Call<CreateJobsResponse>,
                response: Response<CreateJobsResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("success", response.body()?.message.toString())
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = true
                    Log.e("success", response.body()?.data?.jobId.toString())
                    _jobId.value = response.body()?.data?.jobId.toString()
                } else {
                    Log.e("error", response.body()?.message.toString())
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