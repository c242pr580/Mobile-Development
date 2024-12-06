package com.serabutinn.serabutinnn.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.UpdateBioResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateBioViewModel(private val repository: UserRepository) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun updateBio(token: String, name: String?, phone: String?,location: String?) {
        var multipartBody: MultipartBody.Part? = null
        val client = ApiClient.getApiService().updateBiodata("Bearer $token",
            name?.toRequestBody("text/plain".toMediaTypeOrNull()),
            phone?.toRequestBody("text/plain".toMediaTypeOrNull()),
            location?.toRequestBody("text/plain".toMediaTypeOrNull()),
            multipartBody)
        client.enqueue(object : Callback<UpdateBioResponse> {
            override fun onResponse(
                call: Call<UpdateBioResponse>,
                response: Response<UpdateBioResponse>
            ) {
                if (response.isSuccessful) {
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = true
                } else {
                    _message.value = response.body()?.message.toString()
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<UpdateBioResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _message.value = t.message.toString()
                _isSuccess.value = false
            }
        })

    }
}