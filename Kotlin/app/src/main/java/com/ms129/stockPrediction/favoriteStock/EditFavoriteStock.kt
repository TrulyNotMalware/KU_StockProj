package com.ms129.stockPrediction.favoriteStock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.ms129Server.*
import kotlinx.android.synthetic.main.activity_edit_favorite_stock.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditFavoriteStock : AppCompatActivity() {
    val REQUEST_CODE_STOCK = 100
    var jsonArray = arrayOf<String>()
    lateinit var myAPI : IController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_favorite_stock)
        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(IController::class.java)
        init()
    }

    private fun init() {
        searchButton.setOnClickListener {
            val stockCode = findViewById<EditText>(R.id.stockCode)
            val code = stockCode.text.toString()
            StockRepository.getStockInfo(code, ::onStockFetched, ::onError)
        }

        addButton.setOnClickListener {
            progressBarInEditStock.visibility = View.VISIBLE
            val returnIntent = Intent()
            returnIntent.putExtra("jsonArray", jsonArray)
            myAPI.addInterest(DInterestStockPost("1725733313",jsonArray[0], "2019-01-01", jsonArray[1]))
                .enqueue(object: Callback<DResult> {
                    override fun onResponse(call: Call<DResult>, response: Response<DResult>) {
                        Log.d("FavoriteStock ADD :", "SUCCESS")
                        val res = response.body()
                        progressBarInEditStock.visibility = View.GONE
                        finish()
                    }

                    override fun onFailure(call: Call<DResult>, t: Throwable) {
                        Log.e("FavoriteStock ADD :", "FAILED")
                        progressBarInEditStock.visibility = View.GONE
                        finish()
                    }
                })
//            myAPI.deleteInterest(DDeleteStockPost("1725733313",jsonArray[0]))
//                .enqueue(object: Callback<DResult> {
//                    override fun onResponse(call: Call<DResult>, response: Response<DResult>) {
//                        Log.d("FavoriteStock DELETE :", "SUCCESS")
//                        val res = response.body()
//                        if(res!!.Delete == "1"){
//                            setResult(RESULT_OK, returnIntent)
//                            finish()
//                        }
//                        else{
//                            Log.e("FAvoriteStock DELETE(result error):", "FAILED")
//                            finish()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<DResult>, t: Throwable) {
//                        Log.e("FavoriteStock DELETE :", "FAILED")
//                        finish()
//                    }
//                })
        }
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun onError() {
        text_result.text = "???????????? ?????? ?????????????????? ????????? ??????????????????."
    }

    private fun onStockFetched(json: JsonData) {
        Log.d("COde: ", json.toString())
        jsonArray = arrayOf<String>(json.globalQuote.symbol, json.globalQuote.price, json.globalQuote.close)
        addButton.isEnabled = true
        if(json.globalQuote.symbol != null || json.globalQuote.symbol != "null") {
            val str = "?????? ??????: ${json.globalQuote.symbol}\n?????? ??????: ${json.globalQuote.price}\n?????? ??????: ${json.globalQuote.close}"
            text_result.text = str
        }
        else{
            text_result.text = "???????????? ?????? ?????????????????? ????????? ??????????????????."
        }
        if(json.globalQuote.symbol == "null"){
            text_result.text = "???????????? ?????? ?????????????????? ????????? ??????????????????."
        }
    }

    override fun onBackPressed() {
        finish()
    }

}
