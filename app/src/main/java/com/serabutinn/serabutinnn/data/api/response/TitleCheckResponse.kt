package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class TitleCheckResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
