package com.ms129.stockPrediction.ms129Server

import java.io.Serializable

data class AnalyzedStock(
    val date: String,
    val option: String,
    val resultData: List<String>,
    val realData: List<String>,
    val stockCode: String
) : Serializable