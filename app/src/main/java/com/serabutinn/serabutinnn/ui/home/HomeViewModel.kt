package com.serabutinn.serabutinnn.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serabutinn.serabutinnn.data.api.response.HomeResponse

class HomeViewModel : ViewModel() {
    private val _data = MutableLiveData<List<HomeResponse.Data>>()
    val data: LiveData<List<HomeResponse.Data>> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findJobs()
    }
    private fun findJobs(){
        _isLoading.value = true
        val client:
    }
}