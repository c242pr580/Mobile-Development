package com.serabutinn.serabutinnn.data.api

import com.serabutinn.serabutinnn.BuildConfig
import com.serabutinn.serabutinnn.data.api.methods.UserApi
import com.serabutinn.serabutinnn.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        fun getApiService(): UserApi {
            val loggingInterceptor =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)  // Waktu timeout koneksi (30 detik)
                .readTimeout(30, TimeUnit.SECONDS)     // Waktu timeout untuk membaca (30 detik)
                .writeTimeout(30, TimeUnit.SECONDS)    // Waktu timeout untuk menulis (30 detik)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(UserApi::class.java)
        }
    }
}