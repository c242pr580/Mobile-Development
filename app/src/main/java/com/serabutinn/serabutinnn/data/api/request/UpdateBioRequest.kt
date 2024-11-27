package com.serabutinn.serabutinnn.data.api.request

import com.google.gson.annotations.SerializedName
import java.io.File

data class UpdateBioRequest(
    @SerializedName("profilePicture")
    val profilePicture: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("image")
    val name: File? = null
)
