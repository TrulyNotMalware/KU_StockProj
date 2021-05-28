package com.ms129.stockPrediction.recentStock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms129.stockPrediction.R
import kotlinx.android.synthetic.main.activity_recent_analyzed.*

class RecentAnalyzedActivity : AppCompatActivity() {
    lateinit var recentStockAdapter : RecentStockRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_analyzed)

        recentStockAdapter = RecentStockRecyclerAdapter()
        var modelList = ArrayList<RecentStockData>()
//        for (n in news){
//            val myModel = NewsData(title = n.title, date = n.pubDate)
//            modelList.add(myModel)
//        }

        val data1 = RecentStockData("AAPL", "2021-01-01", "3", listOf("1000", "1001"), listOf("1000", "1001"))
        modelList.add(data1)
        recentStockAdapter.submitList(modelList)
        recentStockAdapter.itemClickListener = object: RecentStockRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@RecentAnalyzedActivity, AnalyzedDetailActivity::class.java)
                //intent.putExtra("news_link", news[position].link)
                startActivity(intent)
            }
        }
        recentRecyclerView.layoutManager = LinearLayoutManager(this@RecentAnalyzedActivity, LinearLayoutManager.VERTICAL, false)
        recentRecyclerView.adapter = recentStockAdapter
    }
}
