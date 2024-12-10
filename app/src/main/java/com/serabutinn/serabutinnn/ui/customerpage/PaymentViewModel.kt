package com.serabutinn.serabutinnn.ui.customerpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.serabutinn.serabutinnn.data.api.ApiClient
import com.serabutinn.serabutinnn.data.api.response.CreatePaymentResponse
import com.serabutinn.serabutinnn.data.api.response.DataDetail
import com.serabutinn.serabutinnn.data.api.response.DetailJobResponse
import com.serabutinn.serabutinnn.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel(private val repository: UserRepository) : ViewModel() {
    private val _linkPayment = MutableLiveData<String?>()
    val linkPayment : LiveData<String?> = _linkPayment

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String?>()
    val message :LiveData<String?> = _message

    private val _data = MutableLiveData<DataDetail?>()
    val data: LiveData<DataDetail?> = _data

    fun getSession() = repository.getSession().asLiveData()


    fun getJobDetailCust(token: String, id: String) {
        val client = ApiClient.getApiService().getDetail("Bearer $token", id)
        client.enqueue(object : Callback<DetailJobResponse> {
            override fun onResponse(
                call: Call<DetailJobResponse>, response: Response<DetailJobResponse>
            ) {
                if (response.isSuccessful) {
                    _data.value = response.body()?.data
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailJobResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                _message.value = t.message.toString()
            }
        })
    }
    fun createPayment(token:String,id:String){
        val client = ApiClient.getApiService().createPayment(token="bearer $token", jobId = id)
        client.enqueue(object : Callback<CreatePaymentResponse> {
            override fun onResponse(
                call: Call<CreatePaymentResponse>,
                response: Response<CreatePaymentResponse>
            ) {
                if(response.isSuccessful){
                    _isSuccess.value= true
                    _linkPayment.value = response.body()?.data?.paymentUrl
                }
                else{
                    _isSuccess.value=false
                    _message.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<CreatePaymentResponse>, t: Throwable) {
                t.message?.let { Log.e("Payment failed", it) }
            }

        })
    }
}
