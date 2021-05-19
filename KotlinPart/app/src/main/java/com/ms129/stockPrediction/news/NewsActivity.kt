package com.ms129.stockPrediction.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.naverAPI.Items
import com.ms129.stockPrediction.naverAPI.NaverRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // 어답터 인스턴스 생성
        initNaver()
    }

    private fun initNaver() {
        NaverRepository.getSearchNews("주식", ::onSearchNewsFetched, ::onError)
    }

    private fun onSearchNewsFetched(news: List<Items>) {
        newsRecyclerAdapter = NewsRecyclerAdapter()
        var modelList = ArrayList<NewsData>()
        for (n in news){
            val myModel = NewsData(title = n.title, date = n.pubDate)
            modelList.add(myModel)
        }
        newsRecyclerAdapter.submitList(modelList)
        newsRecyclerAdapter.itemClickListener = object: NewsRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@NewsActivity, NewsDetailActivity::class.java)
                intent.putExtra("news_link", news[position].link)
                startActivity(intent)
            }
        }
        my_recycler_view.layoutManager = LinearLayoutManager(this@NewsActivity, LinearLayoutManager.VERTICAL, false)
        my_recycler_view.adapter = newsRecyclerAdapter

    }

    private fun onError() {
        Toast.makeText(this, "error result", Toast.LENGTH_SHORT).show()
    }
}