package com.serabutinn.serabutinnn.ui.mitrapage.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.data.api.response.ListAllJobsResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<List<DataAllJobs>>()
    val data: LiveData<List<DataAllJobs>> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {

    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun findJobs(user:UserModel){
        _isLoading.value = true
        Log.e("token123",getSession().value.toString())
        val client = ApiClient.getApiService().getHome("Bearer ${user.token}")
        client.enqueue(object:Callback<ListAllJobsResponse>{
            override fun onResponse(
                call: Call<ListAllJobsResponse>,
                response: Response<ListAllJobsResponse>
            ) {
                _isLoading.value=false
                if (!response.isSuccessful){
                    _message.value = response.message().toString()
                }else{
                    _data.value = response.body()?.data?.filterNotNull()
                }
            }
            override fun onFailure(call: Call<ListAllJobsResponse>, t: Throwable) {
                Log.e("error",t.message.toString())
                _isLoading.value = false
            }

        })

    }
}