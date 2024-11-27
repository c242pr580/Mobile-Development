package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName
import java.io.File

data class BiodataResponse(

	@field:SerializedName("data")
	val data: DataBio? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataBio(

	@field:SerializedName("profilePicture")
	val profilePicture: File? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("role_id")
	val roleId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
