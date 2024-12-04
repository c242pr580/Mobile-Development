package com.serabutinn.serabutinnn.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.DataDetail
import com.serabutinn.serabutinnn.data.api.response.DetailJobResponse
import com.serabutinn.serabutinnn.data.api.response.TakeJobResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailJobMitraViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<DataDetail?>()
    val data: LiveData<DataDetail?> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun getSession() = repository.getSession().asLiveData()

    fun getJobDetailMitra(token: String, id: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().getDetail("Bearer $token", id)
        client.enqueue(object : Callback<DetailJobResponse> {
            override fun onResponse(
                call: Call<DetailJobResponse>,
                response: Response<DetailJobResponse>
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
    fun daftarKerja(token: String, id: String) {
        _isLoading.value = true
        val client = ApiClient.getApiService().assignJob("Bearer $token", id)
        client.enqueue(object : Callback<TakeJobResponse> {
            override fun onResponse(
                call: Call<TakeJobResponse>,
                response: Response<TakeJobResponse>
            ) {
                if (response.isSuccessful) {
                    _message.value = response.body()?.message
                    _isSuccess.value = true
                } else {
                    _message.value = response.message()
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<TakeJobResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })

    }
}