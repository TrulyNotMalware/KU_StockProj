package com.ms129.stockPrediction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.favoriteStock.JsonData
import com.ms129.stockPrediction.favoriteStock.StockRepository
import com.ms129.stockPrediction.ms129Server.DAnalyzePost
import com.ms129.stockPrediction.ms129Server.DAnalyzeResult
import com.ms129.stockPrediction.ms129Server.IController
import com.ms129.stockPrediction.ms129Server.RetrofitClient
import com.ms129.stockPrediction.recentStock.AnalyzedDetailActivity
import kotlinx.android.synthetic.main.activity_edit_favorite_stock.*
import kotlinx.android.synthetic.main.activity_stock_analyze.*
import kotlinx.android.synthetic.main.activity_stock_analyze.searchButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockAnalyzeActivity : AppCompatActivity() {
    var checkNum = 0
    var jsonArray = arrayOf<String>()
    var stockCodeNum = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_analyze)

        val retrofit = RetrofitClient.getInstance()
        val myAPI = retrofit.create(IController::class.java)
        searchButton.setOnClickListener {
            val stockCode = findViewById<EditText>(R.id.stockEditView)
            val code = stockCode.text.toString()
            stockCodeNum = code
            StockRepository.getStockInfo(code, ::onStockFetched, ::onError)
        }

        canelButton.setOnClickListener {
            finish()
        }



        check1w.setOnCheckedChangeListener { buttonView, isChecked ->
            resetCheck()
            if(isChecked){
                buttonView.isChecked = true
                checkNum = 0
            }
        }
        check2w.setOnCheckedChangeListener { buttonView, isChecked ->
            resetCheck()
            if(isChecked){
                buttonView.isChecked = true
                checkNum = 1
            }
        }
        check1m.setOnCheckedChangeListener { buttonView, isChecked ->
            resetCheck()
            if(isChecked){
                buttonView.isChecked = true
                checkNum = 2
            }
        }
        check3m.setOnCheckedChangeListener { buttonView, isChecked ->
            resetCheck()
            if(isChecked){
                buttonView.isChecked = true
                checkNum = 3
            }
        }
        check6m.setOnCheckedChangeListener { buttonView, isChecked ->
            resetCheck()
            if(isChecked){
                buttonView.isChecked = true
                checkNum = 4
            }
        }

        analyzeButton.setOnClickListener {
            progressBar2.visibility = ProgressBar.VISIBLE
            UserApiClient.instance.me { user, error ->
                myAPI.analyzeStock(DAnalyzePost(user!!.id.toString(), "2020-01-01", checkNum.toString(), "0", stockCodeNum))
                    .enqueue(object: Callback<DAnalyzeResult> {
                        override fun onResponse(
                            call: Call<DAnalyzeResult>,
                            response: Response<DAnalyzeResult>
                        ) {
                            Log.d("Analyze Stock:", "SUCCESS")
                            val result = response.body()
                            Log.d("Analyze Stock BODY: ", result!!.stockCode + ", "+result!!.date)
                            val intent = Intent(baseContext, AnalyzedDetailActivity::class.java)
                            intent.putExtra("DATA", result)
                            startActivity(intent)
                        }

                        override fun onFailure(call: Call<DAnalyzeResult>, t: Throwable) {
                            Log.e("Analyze Stock: ", "FAILED")
                            progressBar2.visibility = ProgressBar.INVISIBLE
                        }
                    })
            }

        }
    }

    private fun onError() {
        text_result.text = "존재하지 않는 주식명이거나 오류가 발생했습니다."
    }

    private fun onStockFetched(json: JsonData) {
        Log.d("COde: ", json.toString())
        jsonArray = arrayOf<String>(json.globalQuote.symbol, json.globalQuote.price, json.globalQuote.close)
        analyzeButton.isEnabled = true
        val str = "종목 코드: ${json.globalQuote.symbol}\n종목 가격: ${json.globalQuote.price}\n최신 일자: ${json.globalQuote.close}"
        stockInfoView.text = str
    }

    private fun resetCheck() {
        //check1w.isSelected = false
        check1w.isChecked = false
        check2w.isChecked = false
        check1m.isChecked = false
        check3m.isChecked = false
        check6m.isChecked = false
    }
}
