package com.serabutinn.serabutinnn.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.response.BaseResponse
import com.serabutinn.serabutinnn.data.api.response.LoginResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    fun loginUser(email: String, pwd: String) {

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                val loginRequest = LoginRequest(
                    password = pwd,
                    email = email
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    loginResult.value = BaseResponse.Error(response?.message())
                }

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}