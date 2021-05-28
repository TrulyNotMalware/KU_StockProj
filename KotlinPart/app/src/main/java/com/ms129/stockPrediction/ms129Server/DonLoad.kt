package com.ms129.stockPrediction.ms129Server

import java.io.Serializable

data class DonLoad(
    val analyzedStocks: List<AnalyzedStock>,
    val favoriteStocks: List<FavoriteStock>,
    val profileLink: String,
    val userName: String
) : Serializable