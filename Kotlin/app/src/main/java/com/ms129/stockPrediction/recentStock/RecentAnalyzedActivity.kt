package com.ms129.stockPrediction.recentStock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms129.stockPrediction.MainActivity
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.ms129Server.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recent_analyzed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecentAnalyzedActivity : AppCompatActivity() {
    lateinit var analyzedAdapter : AnalyzedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_analyzed)
        val retrofit = RetrofitClient.getInstance()
        val myAPI = retrofit.create(IController::class.java)
        val id = intent.getStringExtra("ID") as String

        myAPI.onLoad(id).enqueue(object: Callback<DonLoad?> {
            override fun onResponse(call: Call<DonLoad?>, response: Response<DonLoad?>) {
                Log.d("[MAIN] OnLoad Success::", response.body().toString())
                val onLoadData = response.body()
                initAnalyzedList(onLoadData?.analyzedStocks)
            }
            override fun onFailure(call: Call<DonLoad?>, t: Throwable) {
                Log.d("[MAIN] OnLoad Failure::", t.toString())
                initAnalyzedList(listOf(AnalyzedStock("nullOrSize0", "0", "0", listOf("0", "1"), listOf("0", "1"),"nullOrSize0")))
            }
        })


    }

    fun initAnalyzedList(analyzedStocks: List<AnalyzedStock>?){
        analyzedAdapter = AnalyzedRecyclerAdapter()

        val temp = ArrayList<AnalyzedStock>()
        var modelList = ArrayList<AnalyzedStock>()
        if(analyzedStocks == null || analyzedStocks.isEmpty()){
            Log.e("[MAIN] AnalyzedSector::", "null or empty")
            val data1 = AnalyzedStock("nullOrSize0", "nullOrSize0", "0", listOf("0", "1"), listOf("0", "1"),"nullOrSize0")
            modelList.add(data1)
            analyzedAdapter.submitList(modelList)
        }
        else{
//            analyzeSampleView.visibility = View.GONE
            analyzedRecyclerView.visibility = View.VISIBLE
            Log.e("[MAIN] AnalyzedSector::", ">=5")
            if(analyzedStocks.size >= 3)
                for(i in 0..2)
                    temp.add(analyzedStocks[i])
            else
                for(stock in analyzedStocks)
                    temp.add(stock)
            modelList = temp
            analyzedAdapter.submitList(modelList)
        }
        analyzedAdapter.itemClickListener = object: AnalyzedRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@RecentAnalyzedActivity, AnalyzedDetailActivity::class.java)
                intent.putExtra("MAIN_ANALYZED_DATA", temp[position])
                startActivity(intent)
            }
        }
        analyzedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        analyzedRecyclerView.adapter = analyzedAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
    }
}
