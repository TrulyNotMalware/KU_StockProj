package com.ms129.stockPrediction.login

import com.google.gson.annotations.SerializedName

data class LoginReturnData(
    @SerializedName("isFirst") var isFirst : String
)