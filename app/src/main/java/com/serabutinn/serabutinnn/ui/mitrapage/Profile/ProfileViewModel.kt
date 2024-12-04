package com.serabutinn.serabutinnn.ui.mitrapage.Profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.DataBio
import com.serabutinn.serabutinnn.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<DataBio?>()
    val data: LiveData<DataBio?> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message




    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
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
                    _data.value = response.body()?.data
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
}