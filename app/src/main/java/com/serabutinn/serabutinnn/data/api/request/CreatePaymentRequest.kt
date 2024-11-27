package com.serabutinn.serabutinnn.data.api.request

import com.google.gson.annotations.SerializedName

data class CreatePaymentRequest(
    @SerializedName("job_id")
    var jobId: String,
)
