package com.serabutinn.serabutinnn.data.api.methods

import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.request.SignupRequest
import com.serabutinn.serabutinnn.data.api.response.LoginResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @POST("/api/authaccount/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/authaccount/signup")
    suspend fun signupUser(@Body signupRequest: SignupRequest): Response<SignupResponse>

    @GET("home")
    suspend fun getHome(): Response<String>


    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }
    }
}