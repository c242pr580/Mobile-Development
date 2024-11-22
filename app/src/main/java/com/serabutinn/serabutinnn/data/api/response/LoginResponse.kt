package com.serabutinn.serabutinnn.data.api.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
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
        @SerializedName("UserId")
        var userId: String,
        @SerializedName("Role")
        var role: String,
        @SerializedName("Name")
        var name: String,
        @SerializedName("Token")
        var token: String,
        @SerializedName("Username")
        var username: String,
    )
}