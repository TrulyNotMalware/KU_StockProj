package com.ms129.stockPrediction.naverAPI

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NaverRepository {

    private val api: INaverAPI //인터페이스 구현

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(INaverAPI::class.java)
    }

    fun getSearchNews(query: String,
                      onSuccess: (news: List<Items>) -> Unit,
                      onError: () -> Unit ) {
        api.getSearchNews(query = query, display=100)
            .enqueue(object : Callback<ResultGetSearchNews> {
                override fun onResponse(
                    call: Call<ResultGetSearchNews>,
                    response: Response<ResultGetSearchNews>
                ) {
//                    Log.d("query::", query)
//                    Log.d("1::", response.toString())
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.items)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<ResultGetSearchNews>, t: Throwable) {
                    onError.invoke()
                }
            })
    }
}