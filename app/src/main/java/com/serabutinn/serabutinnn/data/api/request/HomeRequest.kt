package com.serabutinn.serabutinnn.data.api.request

import com.google.gson.annotations.SerializedName

data class HomeRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)
