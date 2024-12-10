package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class GetDetailMitraResponse(

	@field:SerializedName("data")
	val data: Datamitra3? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Datamitra3(

	@field:SerializedName("business_name")
	val businessName: String? = null,

	@field:SerializedName("transaction_done")
	val transactionDone: String? = null,

	@field:SerializedName("business_address")
	val businessAddress: String? = null,

	@field:SerializedName("mitra_id")
	val mitraId: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null
)
