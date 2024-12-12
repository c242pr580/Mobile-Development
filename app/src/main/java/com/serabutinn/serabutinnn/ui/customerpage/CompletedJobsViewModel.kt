package com.serabutinn.serabutinnn.ui.customerpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.CompleteJobResponse
import com.serabutinn.serabutinnn.data.api.response.DeleteJobsResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletedJobsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isSuccess2 = MutableLiveData<Boolean>()
    val isSuccess2: LiveData<Boolean> = _isSuccess2

    fun getSession() = repository.getSession().asLiveData()
    fun completeJob(token: String, id: String) {
        val client = ApiClient.getApiService().completeJob(token="Bearer $token", id)
        _isLoading.value = true

        client.enqueue(object : Callback<CompleteJobResponse> {
            override fun onResponse(
                call: Call<CompleteJobResponse>,
                response: Response<CompleteJobResponse>
            ) {
                Log.d("Response", "onResponse: $response")
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isSuccess.value = true
                        _message.value = responseBody.message ?: "Job completed successfully."
                    } else {
                        Log.e("ResponseError", "Response body is null")
                        _message.value = "Unexpected error: Response body is null"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResponseError", "Error response: $errorBody")
                    _message.value = errorBody ?: response.message()
                }
            }

            override fun onFailure(call: Call<CompleteJobResponse>, t: Throwable) {
                // Log and handle failure
                Log.e("NetworkError", "Request failed: ${t.message}")
                _isLoading.value = false
                _message.value = "Request failed: ${t.message}"
            }
        })
    }

    fun ratejobs(token:String,rating:String,id:String){
        val client = ApiClient.getApiService().ratingJob(token="Bearer $token",rating,id)
        client.enqueue(object : Callback<DeleteJobsResponse> {
            override fun onResponse(
                call: Call<DeleteJobsResponse>,
                response: Response<DeleteJobsResponse>
            ) {
                if(response.isSuccessful){
                    _isSuccess2.value = true
                    _message.value = response.body()?.message.toString()
                }
                else{
                    _isSuccess2.value = false
                    _message.value = response.errorBody()?.string()
                }
            }

            override fun onFailure(call: Call<DeleteJobsResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _message.value = t.message.toString()
                _isSuccess2.value = false
            }
        })

    }
}