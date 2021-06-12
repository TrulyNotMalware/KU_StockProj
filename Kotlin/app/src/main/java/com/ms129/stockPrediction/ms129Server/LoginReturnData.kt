package com.ms129.stockPrediction.ms129Server

import com.google.gson.annotations.SerializedName

data class LoginReturnData(
    @SerializedName("isFirst") var isFirst : String
)