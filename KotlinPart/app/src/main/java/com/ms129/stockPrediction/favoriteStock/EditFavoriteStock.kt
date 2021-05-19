package com.ms129.stockPrediction.favoriteStock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ms129.stockPrediction.R

class EditFavoriteStock : AppCompatActivity() {
    val REQUEST_CODE_STOCK = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_favorite_stock)
        init()


    }

    private fun init() {
        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val stockCode = findViewById<EditText>(R.id.stockCode)
            val code = stockCode.text.toString()
            StockRepository.getStockInfo(code, ::onStockFetched, ::onError)
        }
    }

    private fun onError() {
        Toast.makeText(this, "error result", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun onStockFetched(json: JsonData) {
        val resultView = findViewById<TextView>(R.id.resultView)
        Log.d("COde: ", json.toString())
        //Log.d("close: ", data.close)
//        val t = "코드: ${json.globalQuote.symbol}  종가: ${json.globalQuote.price}"
//        resultView.text = t
        val jsonArray = arrayOf<String>(json.globalQuote.symbol, json.globalQuote.price, json.globalQuote.close)
        val returnIntent = Intent()
        returnIntent.putExtra("jsonArray", jsonArray)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

}