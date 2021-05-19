package com.ms129.stockPrediction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_stock_analyze_acitivy.*

class StockAnalyzeAcitivy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_analyze_acitivy)

        canelButton.setOnClickListener {
            finish()
        }
    }
}
