package com.serabutinn.serabutinnn.data.api.request

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("email")
    var email: String,
)
