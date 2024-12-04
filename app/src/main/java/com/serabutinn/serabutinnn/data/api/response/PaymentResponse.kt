package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("paymentUrl")
	val paymentUrl: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null
)
