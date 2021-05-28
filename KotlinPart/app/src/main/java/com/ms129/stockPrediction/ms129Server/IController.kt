package com.ms129.stockPrediction.ms129Server

import com.ms129.stockPrediction.favoriteStock.StockData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IController {

    @POST("login")
    fun login(@Body body : LoginBody): Call<LoginReturnData>

    @GET("onLoad")
    fun onLoad(
        @Query("UID") uid : String
    ) : Call<DonLoad>

    @POST("interest/add")
    fun addInterest(
        @Body stockData : DInterestStockPost
    ) : Call<DResult>

    @POST("interest/delete")
    fun deleteInterest(
        @Body stockData : DDeleteStockPost
    ) : Call<DResult>

    @POST("analyze")
    fun analyzeStock(
        @Body stockData : DAnalyzePost
    ) : Call<DAnalyzeResult>
}