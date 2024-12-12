package com.serabutinn.serabutinnn.ui.mitrapage.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.DataAllJobs
import com.serabutinn.serabutinnn.data.api.response.DataBio
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
    private val _dataBio = MutableLiveData<DataBio?>()
    val dataBio: LiveData<DataBio?> = _dataBio

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getBiodata(token:String){
        _isLoading.value = true
        val client = ApiClient.getApiService().getBiodata("Bearer $token")
        client.enqueue(object : Callback<BiodataResponse> {
            override fun onResponse(
                call: Call<BiodataResponse>,
                response: Response<BiodataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.e("Data", response.body()?.data.toString())
                    _dataBio.value = response.body()?.data
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<BiodataResponse>, t: Throwable) {
                Log.e("error2", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })

    }
    fun findJobs(user:UserModel) {
        _isLoading.value = true
        val client = ApiClient.getApiService().getHome("Bearer ${user.token}")
        client.enqueue(object : Callback<ListAllJobsResponse> {
            override fun onResponse(
                call: Call<ListAllJobsResponse>,
                response: Response<ListAllJobsResponse>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    val jobs = response.body()?.data?.filterNotNull()
                    _data.value = jobs!!
                    filterJobs(jobs)
                    _message.value = response.message().toString()
                } else {
                    _data.value = response.body()?.data?.filterNotNull()
                }
            }

            override fun onFailure(call: Call<ListAllJobsResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
            }

        })
    }

    private fun filterJobs(originalList: List<DataAllJobs>?) {
        val filtered = originalList?.filter { it.status == "In Progress" || it.status == "Pending" }
        _data.postValue(filtered!!)
    }

}