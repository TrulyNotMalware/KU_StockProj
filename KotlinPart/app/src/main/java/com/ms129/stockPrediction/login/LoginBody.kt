package com.ms129.stockPrediction.login

import com.google.gson.annotations.SerializedName

data class LoginBody(
    val UID: String,
    val UNAME: String,
    val PROFILELINK: String
)
