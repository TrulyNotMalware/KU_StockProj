package com.ms129.stockPrediction.favoriteStock

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IStockRequest {
    @GET("query")
    fun getStockInfo(
        @Query("function") function: String,
        @Query("symbol") symbol: String,
        @Query("apikey") apikey: String
    ): Call<JsonData>
}