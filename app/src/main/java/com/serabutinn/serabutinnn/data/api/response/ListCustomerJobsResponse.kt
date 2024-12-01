package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class ListCustomerJobsResponse(

	@field:SerializedName("data")
	val data: List<DataJobsCustomer?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataJobsCustomer(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("cost")
	val cost: String? = null,

	@field:SerializedName("job_id")
	val jobId: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("deadline")
	val deadline: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("completedAt")
	val completedAt: String? = null,

	@field:SerializedName("mitra_id")
	val mitraId: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("canceledAt")
	val canceledAt: String? = null
)