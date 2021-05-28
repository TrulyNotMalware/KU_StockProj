package com.ms129.stockPrediction.recentStock

data class RecentStockData(
    val stockCode : String, // 종목 코드
    val date : String, // 분석 날짜
    val option : String, // 사용자 옵션
    val resultData : List<String>, // 분석 결과
    val realData : List<String>
)

data class Stock(
    var index: Int = 0,
    var price: Long = 0
)