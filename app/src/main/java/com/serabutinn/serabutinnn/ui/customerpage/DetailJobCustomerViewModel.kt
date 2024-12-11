package com.serabutinn.serabutinnn.ui.customerpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.DataDetail
import com.serabutinn.serabutinnn.data.api.response.DeleteJobsResponse
import com.serabutinn.serabutinnn.data.api.response.DetailJobResponse
import com.serabutinn.serabutinnn.data.api.response.VerifyFaceResponse

import com.serabutinn.serabutinnn.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailJobCustomerViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<DataDetail?>()
    val data: LiveData<DataDetail?> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _isVerified = MutableLiveData<Boolean>()
    val isVerified: LiveData<Boolean> = _isVerified

    fun getSession() = repository.getSession().asLiveData()
    fun getJobDetailCust(token: String, id: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().getDetail("Bearer $token", id)
        client.enqueue(object : Callback<DetailJobResponse> {
            override fun onResponse(
                call: Call<DetailJobResponse>, response: Response<DetailJobResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _data.value = response.body()?.data
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailJobResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    fun deleteJob(id: String, token: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().deleteJob("Bearer $token", id)
        client.enqueue(object : Callback<DeleteJobsResponse> {
            override fun onResponse(
                call: Call<DeleteJobsResponse>, response: Response<DeleteJobsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _message.value = response.body()?.message.toString()
                } else {
                    _message.value = response.errorBody().toString()
                    Log.e("error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<DeleteJobsResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })


    }

    fun uploadFace(token: String, image: File) {
        _isLoading.value=true
        var multipartBody: MultipartBody.Part? = null
        val requestImageFile = image.asRequestBody("image/jpg".toMediaType())
        multipartBody = requestImageFile.let {
            MultipartBody.Part.createFormData(
                "input_image", image.name, it
            )
        }
        val client = ApiClient.getApiService().verifyFace(token = "Bearer $token", multipartBody)
        client.enqueue(object : Callback<VerifyFaceResponse> {
            override fun onResponse(
                call: Call<VerifyFaceResponse>, response: Response<VerifyFaceResponse>
            ) {
                _isLoading.value=false
                if (response.isSuccessful) {
                    _isVerified.value = response.body()?.data?.verified == true
                    if (_isVerified.value == false) {
                        _message.value = "Face not match"
                    }
                }
            }

            override fun onFailure(call: Call<VerifyFaceResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
                _isVerified.value = false
                _message.value = t.message.toString()
            }
        })
    }
}