package com.ms129.stockPrediction.favoriteStock

import com.google.gson.annotations.SerializedName

data class JsonData(@SerializedName("Global Quote") var globalQuote: StockData){}