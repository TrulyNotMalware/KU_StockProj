package com.ms129.stockPrediction.ms129Server

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "8f2e5d3e5e5e0a3363b37629968469d7")
    }
}