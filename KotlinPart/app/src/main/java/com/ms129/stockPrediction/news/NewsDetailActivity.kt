package com.ms129.stockPrediction.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.ms129.stockPrediction.R
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        init()
    }

    private fun init() {
        val detailIntent = intent
        val news_link = detailIntent.getStringExtra("news_link")
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        webView.loadUrl(news_link.toString())
    }
}
