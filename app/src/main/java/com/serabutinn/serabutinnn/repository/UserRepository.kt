package com.serabutinn.serabutinnn.repository

import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.UserPreference
import com.serabutinn.serabutinnn.data.api.methods.UserApi
import com.serabutinn.serabutinnn.data.api.response.CreateJobsResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
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
