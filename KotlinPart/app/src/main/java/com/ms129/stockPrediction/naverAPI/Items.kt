package com.ms129.stockPrediction.naverAPI

import com.google.gson.annotations.SerializedName
import java.util.*

data class Items (
    @SerializedName("title") var title: String = "",
    @SerializedName("originallink") var originallink: String = "",
    @SerializedName("link") var link: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("pubDate") var pubDate: String = ""
){}