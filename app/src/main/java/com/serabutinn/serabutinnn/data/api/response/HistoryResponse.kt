package com.serabutinn.serabutinnn.data.api.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("status")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String
) {
    data class Data(
        @SerializedName("isinya")
        var isinya: String,
    )
}
