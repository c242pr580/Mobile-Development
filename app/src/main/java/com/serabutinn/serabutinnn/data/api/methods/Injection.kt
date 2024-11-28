package com.serabutinn.serabutinnn.data.api.methods

import android.content.Context
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserPreference
import com.serabutinn.serabutinnn.data.api.dataStore
import com.serabutinn.serabutinnn.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiClient.getApiService()
        return UserRepository.getInstance(pref,apiService)
    }
}