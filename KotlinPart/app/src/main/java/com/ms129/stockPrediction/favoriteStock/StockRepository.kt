package com.ms129.stockPrediction.favoriteStock

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StockRepository {

    private val stockRequest: IStockRequest //인터페이스 구현

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.alphavantage.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stockRequest = retrofit.create(IStockRequest::class.java)
    }

    fun getStockInfo(symbol: String,
                     onSuccess: (jsonData: JsonData) -> Unit,
                     onError: () -> Unit) {
        stockRequest.getStockInfo(function = "GLOBAL_QUOTE", symbol = symbol, apikey = "G8GHDZ8EITN82OXG").enqueue(object : Callback<JsonData>{
                override fun onResponse(call: Call<JsonData>, response: Response<JsonData>) {
//                    Log.d("symbol::", symbol)
//                    Log.d("1::", response.toString())
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
//                            Log.d("change::", responseBody.change)
                            onSuccess.invoke(responseBody)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<JsonData>, t: Throwable) {
                    Log.e("ERROR:", t.toString())
                    onError.invoke()
                }
            })
    }
}