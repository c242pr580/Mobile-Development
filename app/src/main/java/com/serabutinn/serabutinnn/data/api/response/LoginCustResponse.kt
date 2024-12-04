package com.serabutinn.serabutinnn.data.api.response
import com.google.gson.annotations.SerializedName

data class LoginCustResponse(
    @field:SerializedName("data")
    val data: DataCust?,

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("error")
    val error: Boolean?,

    @field:SerializedName("status")
    val status: Int?
)

data class DataCust(

    @field:SerializedName("createdAt")
    val createdAt: String?,

    @field:SerializedName("role_id")
    val roleId: Int?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("customer_id")
    val customerId: String?,

    @field:SerializedName("mitra_id")
    val mitraId: String?,

    @field:SerializedName("user_id")
    val userId: String?,

    @field:SerializedName("email")
    val email: String?,

    @field:SerializedName("token")
    val token: String?,

    @field:SerializedName("username")
    val username: String?
)
