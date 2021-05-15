package com.ms129.stockPrediction

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IController {

    @POST("test")
    public fun test(@Body dataClass: DataClass?): Call<DataClass?>?

    @GET("test")
    public fun getTest(): Call<String?>?

}