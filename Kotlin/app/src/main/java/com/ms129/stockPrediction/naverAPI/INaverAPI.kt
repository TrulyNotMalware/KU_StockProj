package com.ms129.stockPrediction.naverAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface INaverAPI {
    @GET("v1/search/news.json")
    fun getSearchNews(
        @Header("X-Naver-Client-Id") clientId: String = "1JrdpMbdXMKHW7_F5KGQ",
        @Header("X-Naver-Client-Secret") clientSecret: String = "MIPrYNEXIl",
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null
    ): Call<ResultGetSearchNews>
}