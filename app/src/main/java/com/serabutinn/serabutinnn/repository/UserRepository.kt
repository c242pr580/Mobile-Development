package com.serabutinn.serabutinnn.repository

import com.serabutinn.serabutinnn.data.api.methods.UserApi
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.request.SignupRequest
import com.serabutinn.serabutinnn.data.api.response.LoginResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import retrofit2.Response

class UserRepository {

   suspend fun loginUser(loginRequest:LoginRequest): Response<LoginResponse>? {
      return  UserApi.getApi()?.loginUser(loginRequest = loginRequest)
    }
    suspend fun signupUser(signupRequest: SignupRequest): Response<SignupResponse>? {
        return  UserApi.getApi()?.signupUser(signupRequest = signupRequest)
    }
}