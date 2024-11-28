package com.serabutinn.serabutinnn.repository

import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.UserPreference
import com.serabutinn.serabutinnn.data.api.methods.UserApi
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.request.SignupRequest
import com.serabutinn.serabutinnn.data.api.request.UpdateBioRequest
import com.serabutinn.serabutinnn.data.api.response.BiodataResponse
import com.serabutinn.serabutinnn.data.api.response.LoginCustResponse
import com.serabutinn.serabutinnn.data.api.response.LoginMitraResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.data.api.response.UpdateBioResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val userApi: UserApi
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

//    suspend fun loginCustomer(email: String, password: String): Response<LoginCustResponse>? {
//        return userApi.loginCustomer(email = email, password = password)
//    }
//
//    suspend fun loginMitra(loginRequest: LoginRequest): Response<LoginMitraResponse>? {
//        return userApi.loginMitra(loginRequest = loginRequest)
//    }
//
//    suspend fun signupUser(signupRequest: SignupRequest): Response<SignupResponse>? {
//        return userApi.signupUser(signupRequest = signupRequest)
//    }
//
//    suspend fun getBiodata(token: String): Response<BiodataResponse>? {
//        return userApi.getBiodata(token = token)
//    }
//
//    suspend fun updateBiodata(
//        updateBioRequest: UpdateBioRequest,
//        token: String
//    ): Response<UpdateBioResponse>? {
//        return userApi.updateBiodata(updateBioRequest = updateBioRequest, token = token)
//
//    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            userApi: UserApi
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, userApi)
            }.also { instance = it }
    }
}
