package com.serabutinn.serabutinnn.ui.customerpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.DataDetail
import com.serabutinn.serabutinnn.data.api.response.DetailJobResponse
import com.serabutinn.serabutinnn.data.api.response.TitleCheckResponse
import com.serabutinn.serabutinnn.data.api.response.UpdateJobsResponse
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

class UpdateJobViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<DataDetail?>()
    val data: LiveData<DataDetail?> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun getSession() = repository.getSession().asLiveData()

    fun updateJob(
        token: String,
        title: String,
        description: String,
        deadline: String,
        cost: String,
        location: String,
        image: File?,
        id: String
    ) {
        _isLoading.value = true
        var multipartBody: MultipartBody.Part? = null
        if (image != null) {
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            multipartBody = requestImageFile.let {
                MultipartBody.Part.createFormData(
                    "image",
                    image.name,
                    it
                )
            }
        }
        val client = ApiClient.getApiService().updateJob(
            token = "Bearer $token",
            title = title.toRequestBody("text/plain".toMediaTypeOrNull()),
            description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
            deadline = deadline.toRequestBody("text/plain".toMediaTypeOrNull()),
            price = cost.toRequestBody("text/plain".toMediaTypeOrNull()),
            location = location.toRequestBody("text/plain".toMediaTypeOrNull()),
            image = multipartBody,
            jobId = id
        )
        client.enqueue(object : Callback<UpdateJobsResponse> {
            override fun onResponse(
                call: Call<UpdateJobsResponse>,
                response: Response<UpdateJobsResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = true
                } else {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<UpdateJobsResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
                _isSuccess.value = false
            }
        })
    }

    fun getJobDetail(token: String, id: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().getDetail("Bearer $token",id)
        client.enqueue(object : Callback<DetailJobResponse> {
            override fun onResponse(
                call: Call<DetailJobResponse>,
                response: Response<DetailJobResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _data.value = response.body()?.data
                }else{
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<DetailJobResponse>, t: Throwable) {
                Log.e("error",t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }
    fun checkTitle(
        token: String,
        title: String,
        description: String,
        deadline: String,
        price: String,
        location: String,
        imageFile: File?,
        id: String
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
                        updateJob(token,title,description,deadline,price,location,imageFile,id)
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
}