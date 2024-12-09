package com.serabutinn.serabutinnn.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaceCameraViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadFace(token: String, image: MultipartBody.Part) {
        val client = ApiClient.getApiService().uploadFace(token = "Bearer $token", image)
        client.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if(response.isSuccessful){
                    Log.e("success22", response.body()?.message.toString())
                    _isSuccess.value = true
                }else{Log.e("error2", response.body()?.message.toString())}
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isSuccess.value = false
            }
        })
    }
}