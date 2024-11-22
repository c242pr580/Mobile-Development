package com.serabutinn.serabutinnn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.serabutinn.serabutinnn.data.api.request.SignupRequest
import com.serabutinn.serabutinnn.data.api.response.BaseResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    val userRepo = UserRepository()
    val signupResult: MutableLiveData<BaseResponse<SignupResponse>> = MutableLiveData()

    fun signupUser(email: String, pwd: String,username:String,name:String,role_id:String,location:String,phone:String) {
        signupResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                val signupRequest = SignupRequest(
                    password = pwd,
                    email = email,
                    username = username,
                    name = name,
                    roleId = role_id,
                    location = location,
                    phone = phone

                )
                val response = userRepo.signupUser(signupRequest = signupRequest)
                if (response?.code() == 201) {
                    signupResult.value = BaseResponse.Success(response.body())
                } else {
                    signupResult.value = BaseResponse.Error(response?.message())
                }

            } catch (ex: Exception) {
                signupResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}