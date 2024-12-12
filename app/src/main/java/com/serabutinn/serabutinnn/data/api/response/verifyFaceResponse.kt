package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class VerifyFaceResponse(

	@field:SerializedName("data")
	val data: Dataverif? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Dataverif(

	@field:SerializedName("verified")
	val verified: Boolean? = null,

	@field:SerializedName("threshold")
	val threshold: Any? = null,

	@field:SerializedName("verification_score")
	val verificationScore: String? = null
)
