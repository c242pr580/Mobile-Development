package com.serabutinn.serabutinnn.viewmodel

import android.R.attr
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.request.LoginRequest
import com.serabutinn.serabutinnn.data.api.response.LoginCustResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun loginCustomer(email: String, password: String) {
        val client = ApiClient.getApiService().loginCustomer(email, password)
        client.enqueue(object : Callback<LoginCustResponse> {
            override fun onFailure(call: Call<LoginCustResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _loggedIn.value = false
            }

            override fun onResponse(
                call: Call<LoginCustResponse>,
                response: Response<LoginCustResponse>
            ) {
                Log.e("success", response.toString())
                if (!response.isSuccessful) {
                    _loggedIn.value = false
                    _message.value = response.message().toString()
                } else {
                    saveSession(
                        UserModel(
                            email,
                            response.body()?.data?.customerId.toString(),
                            response.body()?.data?.roleId.toString(),
                            response.body()?.data?.token.toString(),
                            true
                        )
                    )
                    _loggedIn.value = true
                }
            }
        })
    }
}