package com.serabutinn.serabutinnn.data.api.request

import com.google.gson.annotations.SerializedName
import java.io.File

data class CreateJobsRequest(
    @SerializedName("title")
    var title: String,
    @SerializedName("cost")
    var cost: Int,
    @SerializedName("deadline")
    var deadline: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("image")
    val name: File? = null
)
