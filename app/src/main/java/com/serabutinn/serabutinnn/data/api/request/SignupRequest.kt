package com.serabutinn.serabutinnn.data.api.request


import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("role_id")
    var roleId: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("phone")
    var phone: String
)