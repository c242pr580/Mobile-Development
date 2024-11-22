package com.serabutinn.serabutinnn.data.api.response


import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("status")
    var code: Int,
    @SerializedName("message")
    var message: String
)