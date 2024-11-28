package com.serabutinn.serabutinnn.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serabutinn.serabutinnn.data.api.response.DataDetail

class DetailJobViewModel: ViewModel() {
    private val _data = MutableLiveData<List<DataDetail>>()
    val data: LiveData<List<DataDetail>> = _data
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

}