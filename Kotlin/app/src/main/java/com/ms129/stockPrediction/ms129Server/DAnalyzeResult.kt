package com.ms129.stockPrediction.ms129Server

import java.io.Serializable

data class DAnalyzeResult(
    val date: String,
    val option: String,
    val option2: String,
    val realData: List<String>,
    val resultData: List<String>,
    val stockCode: String
) : Serializable