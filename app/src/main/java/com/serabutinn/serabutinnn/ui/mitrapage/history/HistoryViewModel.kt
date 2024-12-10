package com.serabutinn.serabutinnn.ui.mitrapage.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.data.api.response.DataJobsMitra
import com.serabutinn.serabutinnn.data.api.response.ListCustomerJobsResponse
import com.serabutinn.serabutinnn.data.api.response.ListJobsMitraResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<List<DataJobsMitra?>?>()
    val data: LiveData<List<DataJobsMitra?>?> = _data
    private val _dataCustomer = MutableLiveData<List<DataJobsCustomer?>?>()
    val dataCustomer: LiveData<List<DataJobsCustomer?>?> = _dataCustomer
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getSession() = repository.getSession().asLiveData()

    fun getCustomerJobs(token: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().getCustomerJobs("Bearer $token")
        client.enqueue(object : Callback<ListCustomerJobsResponse> {
            override fun onResponse(
                call: Call<ListCustomerJobsResponse>,
                response: Response<ListCustomerJobsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataCustomer.value = response.body()?.data
                }else{
                    Log.e("DashboardViewModel", "onFailure: ${response.errorBody()}")
                    _message.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ListCustomerJobsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("DashboardViewModel", "onFailure: ${t.message.toString()}")
                _message.value = "onFailure: ${t.message.toString()}"
            }

        })
    }

    fun getMitraJobs(token: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().getMitraJobs("Bearer $token")
        client.enqueue(object : Callback<ListJobsMitraResponse> {
            override fun onResponse(
                call: Call<ListJobsMitraResponse>,
                response: Response<ListJobsMitraResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _data.value = response.body()?.data
                }
            }

            override fun onFailure(call: Call<ListJobsMitraResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("DashboardViewModel", "onFailure: ${t.message.toString()}")
                _message.value = "onFailure: ${t.message.toString()}"
            }
        })
    }
}