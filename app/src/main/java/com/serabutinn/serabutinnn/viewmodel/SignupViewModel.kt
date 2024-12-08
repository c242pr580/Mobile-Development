package com.serabutinn.serabutinnn.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.UserModel
import com.serabutinn.serabutinnn.data.api.response.LoginCustResponse
import com.serabutinn.serabutinnn.data.api.response.SignupResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    private val _signed = MutableLiveData<Boolean>()
    val signed: LiveData<Boolean> = _signed
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun signUp(
        email: String,
        password: String,
        name: String,
        username: String,
        phone: String,
        location: String,
        roleid: Int
    ) {
        val client = ApiClient.getApiService().signupUser(
            username = username,
            name = name,
            email = email,
            password = password,
            phone = phone,
            location = location,
            roleid = roleid.toString()
        )
        client.enqueue(object : Callback<SignupResponse> {
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _error.value = t.message.toString()
            }

            override fun onResponse(
                call: Call<SignupResponse>, response: Response<SignupResponse>
            ) {
                var errorBodys = "ASdf"
                response.errorBody()?.let { errorBodys = it.string() }

                Log.e("error2", errorBodys)
                try {
                    val jsonObject = JSONObject(errorBodys)
                    _error.value = jsonObject.getString("message")
                    // Handle the error message
                } catch (e: JSONException) {
                    // Handle JSON parsing error
                }
                if (response.isSuccessful) {
                    _signed.value = true
                }
            }
        })
    }

    fun loginCustomer(email: String, password: String) {
        val client = ApiClient.getApiService().loginCustomer(email, password)
        client.enqueue(object : Callback<LoginCustResponse> {
            override fun onFailure(call: Call<LoginCustResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _loggedIn.value = false
            }

            override fun onResponse(
                call: Call<LoginCustResponse>, response: Response<LoginCustResponse>
            ) {
                if (!response.isSuccessful) {
                    _loggedIn.value = false
                    _message.value = response.message()
                    return
                }
                Log.e("success", response.body()?.data?.token.toString())
                Log.e("success", response.body()?.data?.roleId.toString())
                val userdata = UserModel(email,
                    (response.body()?.data?.customerId?.filter { true }
                        ?: "") + (response.body()?.data?.mitraId?.filter { true } ?: ""),

                    response.body()?.data?.roleId.toString(),
                    response.body()?.data?.token.toString(),
                    true,
                    response.body()?.data?.name.toString(),
                    response.body()?.data?.customerId.toString())
                Log.e("success", userdata.id)
                saveSession(userdata)
                _loggedIn.value = true
            }
        })
    }
}