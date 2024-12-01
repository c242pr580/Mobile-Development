package com.serabutinn.serabutinnn.ui.customerpage.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.data.api.response.DataJobsCustomer
import com.serabutinn.serabutinnn.data.api.response.ListCustomerJobsResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeCustomerViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<List<DataJobsCustomer>>()
    val data: LiveData<List<DataJobsCustomer>> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getSession(): LiveData<UserModel> { return repository.getSession().asLiveData() }

    fun findJobs(user:UserModel){
        _isLoading.value = true
        val client = ApiClient.getApiService().getCustomerJobs("Bearer ${user.token}")
        client.enqueue(object: Callback<ListCustomerJobsResponse> {
            override fun onResponse(
                call: Call<ListCustomerJobsResponse>,
                response: Response<ListCustomerJobsResponse>
            ) {
                _isLoading.value=false
                if (!response.isSuccessful){
                    _message.value = response.message().toString()
                }else{
                    _data.value = response.body()?.data?.filterNotNull()
                }
            }
            override fun onFailure(call: Call<ListCustomerJobsResponse>, t: Throwable) {
                Log.e("error",t.message.toString())
                _isLoading.value = false
            }

        })

    }
}