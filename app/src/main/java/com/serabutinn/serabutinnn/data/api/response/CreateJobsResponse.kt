package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class CreateJobsResponse(

	@field:SerializedName("data")
	val data: DataCreateJobs? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataCreateJobs(

	@field:SerializedName("image")
	val image: String? = null,

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
	val status: String? = null
)
