package com.ms129.stockPrediction.ms129Server

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var instance : Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    fun getInstance() : Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl("http://203.252.166.247:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}
