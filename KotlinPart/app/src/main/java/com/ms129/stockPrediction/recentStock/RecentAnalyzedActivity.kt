package com.ms129.stockPrediction.recentStock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.ms129Server.AnalyzedStock
import kotlinx.android.synthetic.main.activity_recent_analyzed.*

class RecentAnalyzedActivity : AppCompatActivity() {
    lateinit var analyzedAdapter : AnalyzedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_analyzed)

        analyzedAdapter = AnalyzedRecyclerAdapter()
        var modelList = ArrayList<AnalyzedStock>()
//        for (n in news){
//            val myModel = NewsData(title = n.title, date = n.pubDate)
//            modelList.add(myModel)
//        }

        val data1 = AnalyzedStock("nullOrSize0", "nullOrSize0", listOf("0", "1"), listOf("0", "1"),"nullOrSize0")
        modelList.add(data1)
        analyzedAdapter.submitList(modelList)
        analyzedAdapter.itemClickListener = object: AnalyzedRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@RecentAnalyzedActivity, AnalyzedDetailActivity::class.java)
                //intent.putExtra("news_link", news[position].link)
                //startActivity(intent)
                // Log.d("Analyze Stock BODY: ", result!!.stockCode + ", "+result!!.date)
//                val intent = Intent(this@RecentAnalyzedActivity, AnalyzedDetailActivity::class.java)
//                intent.putExtra("DATA", result)
                startActivity(intent)
            }
        }
        analyzedRecyclerView.layoutManager = LinearLayoutManager(this@RecentAnalyzedActivity, LinearLayoutManager.VERTICAL, false)
        analyzedRecyclerView.adapter = analyzedAdapter
    }
}
