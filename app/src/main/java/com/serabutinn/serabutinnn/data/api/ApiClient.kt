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
            // Logging interceptor untuk memantau request dan response
            val loggingInterceptor =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

            // Membuat OkHttpClient dengan timeout yang disesuaikan
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)  // Waktu timeout koneksi (30 detik)
                .readTimeout(30, TimeUnit.SECONDS)     // Waktu timeout untuk membaca (30 detik)
                .writeTimeout(30, TimeUnit.SECONDS)    // Waktu timeout untuk menulis (30 detik)
                .build()

            // Membuat Retrofit instance dengan OkHttpClient
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)  // Menambahkan client dengan timeout ke Retrofit
                .build()

            // Mengembalikan instance dari UserApi
            return retrofit.create(UserApi::class.java)
        }
    }
}