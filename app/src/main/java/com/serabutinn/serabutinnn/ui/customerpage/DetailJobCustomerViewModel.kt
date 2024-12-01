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
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailJobCustomerViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<DataDetail?>()
    val data: LiveData<DataDetail?> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun getSession() = repository.getSession().asLiveData()

    fun getJobDetailCust(token: String, id: String) {
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

    fun deleteJob(id: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().deleteJob("Bearer ${getSession().value?.token}",id)
        client.enqueue(object : Callback<DeleteJobsResponse> {
            override fun onResponse(
                call: Call<DeleteJobsResponse>,
                response: Response<DeleteJobsResponse>
            ) {
                _isLoading.value= false
                if(response.isSuccessful){
                    _isSuccess.value = true
                    _message.value = response.body()?.message.toString()
                }else{
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DeleteJobsResponse>, t: Throwable) {
                Log.e("error",t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }

        })


    }
}