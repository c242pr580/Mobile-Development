package com.serabutinn.serabutinnn.repository

import com.serabutinn.serabutinnn.data.api.methods.UserApi
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.request.SignupRequest
import com.serabutinn.serabutinnn.data.api.request.UpdateBioRequest
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.LoginCustResponse
import com.serabutinn.serabutinnn.data.api.response.LoginMitraResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.data.api.response.UpdateBioResponse
import retrofit2.Response

class UserRepository {

   suspend fun loginCustomer(loginRequest:LoginRequest): Response<LoginCustResponse>? {
      return  UserApi.getApi()?.loginCustomer(loginRequest = loginRequest)
    }
    suspend fun loginMitra(loginRequest:LoginRequest): Response<LoginMitraResponse>? {
        return  UserApi.getApi()?.loginMitra(loginRequest = loginRequest)
    }
    suspend fun signupUser(signupRequest: SignupRequest): Response<SignupResponse>? {
        return  UserApi.getApi()?.signupUser(signupRequest = signupRequest)
    }
    suspend fun getBiodata(): Response<BiodataResponse>? {
        return UserApi.getApi()?.getBiodata()
    }
    suspend fun updateBiodata(updateBioRequest : UpdateBioRequest): Response<UpdateBioResponse>? {
        return  UserApi.getApi()?.updateBiodata(updateBioRequest = updateBioRequest)

    }
}