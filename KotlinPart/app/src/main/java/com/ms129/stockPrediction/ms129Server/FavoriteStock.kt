package com.ms129.stockPrediction.ms129Server

import java.io.Serializable

data class FavoriteStock(
    val date: String,
    val stockCode: String,
    val realFirst: String,
    val predictLast: String
): Serializable