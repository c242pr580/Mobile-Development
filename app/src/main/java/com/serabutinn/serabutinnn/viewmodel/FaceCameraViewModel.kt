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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FaceCameraViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadFace(token: String, image: File) {
        var multipartBody: MultipartBody.Part? = null
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        multipartBody = requestImageFile.let {
            MultipartBody.Part.createFormData(
                "image", image.name, it
            )
        }
        val client = ApiClient.getApiService().uploadFace(token = "Bearer $token", multipartBody)
        client.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if(response.isSuccessful){
                    _isSuccess.value = true
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _isSuccess.value = false
            }
        })
    }
}