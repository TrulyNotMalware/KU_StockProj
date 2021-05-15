package com.ms129.stockPrediction.naverAPI

import com.google.gson.annotations.SerializedName

data class ResultGetSearchNews (
    @SerializedName("lastBuildDate") var lastBuildDate: String = "",
    @SerializedName("total") var total: Int = 0,
    @SerializedName("start") var start: Int = 0,
    @SerializedName("display") var display: Int = 0,
    @SerializedName("items") var items: List<Items>
){}