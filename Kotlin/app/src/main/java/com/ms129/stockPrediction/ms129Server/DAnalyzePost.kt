package com.ms129.stockPrediction.ms129Server

data class DAnalyzePost(
    val UID: String,
    val date: String,
    val option: String,
    val option2: String,
    val stockCode: String
)