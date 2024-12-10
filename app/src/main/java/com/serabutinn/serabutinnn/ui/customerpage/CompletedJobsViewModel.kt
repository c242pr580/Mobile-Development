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
    fun completeJob(token:String,id:String){
        val client = ApiClient.getApiService().completeJob(token,id)
        _isLoading.value = true
        client.enqueue(object : Callback<CompleteJobResponse> {
            override fun onResponse(
                call: Call<CompleteJobResponse>,
                response: Response<CompleteJobResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    _isSuccess.value = true
                    _message.value = response.body()?.message.toString()
                }
                else{
                    _isLoading.value = false
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<CompleteJobResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }
    fun ratejobs(token:String,rating:String,id:String){
        val client = ApiClient.getApiService().ratingJob(token,rating,id)
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