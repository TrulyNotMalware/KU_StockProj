package com.ms129.stockPrediction.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IController {

    @POST("test")
    fun test(@Body dataClass: DataClass?): Call<DataClass?>?

    @GET("test")
    fun getTest(): Call<String?>?

    @POST("login")
    fun login(@Body body : LoginBody): Call<LoginReturnData>

    @POST("onLoad")
    fun onLoad(
        @Body dataClass2 : DataClass2
    ): Call<FavoriteStockData?>
}