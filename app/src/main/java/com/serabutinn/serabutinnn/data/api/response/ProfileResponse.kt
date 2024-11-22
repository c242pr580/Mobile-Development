package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("status")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String
) {
    data class Data(
        @SerializedName("Email")
        var email: String,
        @SerializedName("userId")
        var userId: String,
        @SerializedName("role")
        var role: String,
        @SerializedName("Name")
        var name: String,
        @SerializedName("Token")
        var token: String,
        @SerializedName("username")
        var username: String,
    )
}