package com.ms129.stockPrediction.favoriteStock

import com.google.gson.annotations.SerializedName

data class StockData(
    @SerializedName("01. symbol") var symbol: String,
    @SerializedName("02. open") var open: String,
    @SerializedName("03. high") var high: String,
    @SerializedName("04. low") var low: String,
    @SerializedName("05. price") var price: String,
    @SerializedName("06. volume") var volume: String,
    @SerializedName("07. latest trading day") var close: String,
    @SerializedName("08. previous close") var previousClose: String,
    @SerializedName("09. change") var change: String,
    @SerializedName("10. change percent") var changePercent: String

){}