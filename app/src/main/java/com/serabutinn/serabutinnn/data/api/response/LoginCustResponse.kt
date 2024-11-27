package com.serabutinn.serabutinnn.data.api.response
import com.google.gson.annotations.SerializedName

data class LoginCustResponse(
    @field:SerializedName("data")
    val data: DataCust? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class DataCust(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("role_id")
    val roleId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("customer_id")
    val customerId: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("username")
    val username: String? = null
)
