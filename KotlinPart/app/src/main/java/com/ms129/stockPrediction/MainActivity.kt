package com.ms129.stockPrediction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.favoriteStock.FavoriteStockActivity
import com.ms129.stockPrediction.ms129Server.DonLoad
import com.ms129.stockPrediction.ms129Server.IController
import com.ms129.stockPrediction.ms129Server.RetrofitClient
import com.ms129.stockPrediction.naverAPI.INaverAPI
import com.ms129.stockPrediction.naverAPI.Items
import com.ms129.stockPrediction.naverAPI.NaverRepository
import com.ms129.stockPrediction.news.NewsActivity
import com.ms129.stockPrediction.news.NewsDetailActivity
import com.ms129.stockPrediction.recentStock.AnalyzedDetailActivity
import com.ms129.stockPrediction.recentStock.RecentAnalyzedActivity
import com.ms129.stockPrediction.recentStock.RecentStockData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var myAPI : IController
    lateinit var naverAPI : INaverAPI
    lateinit var favoriteRecyclerAdapter : FavoriteRecyclerAdapter
    val REQUEST_LIST_STOCK = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(IController::class.java)
        init()
        initNaver()
        initView()
    }


    private fun init() {
        val receivedIntent = intent
      //  val onLoadData = receivedIntent.getSerializableExtra("ON_LOAD_DATA") as DonLoad
//        val str = onLoadData.favoriteStocks[0].stockCode + "/" + onLoadData.favoriteStocks[0].price + "/" + onLoadData.favoriteStocks[0].date
//        stock_view1.text = str
        //val str2 = onLoadData.analyzedStocks[0].stockCode + "/" + onLoadData.analyzedStocks[0].option + "/" + onLoadData.analyzedStocks[0].date
        //recentAnalyzeView1.text = str2
        favoriteRecyclerAdapter = FavoriteRecyclerAdapter()
        var modelList = ArrayList<RecentStockData>()
//        for (n in news){
//            val myModel = NewsData(title = n.title, date = n.pubDate)
//            modelList.add(myModel)
//        }

        val data1 = RecentStockData("AAPL", "2021-01-01", "3", listOf("1000", "1001"), listOf("1030", "1001"))
        modelList.add(data1)
        favoriteRecyclerAdapter.submitList(modelList)
        favoriteRecyclerAdapter.itemClickListener = object: FavoriteRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {

            }
        }
        interestRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        interestRecyclerView.adapter = favoriteRecyclerAdapter

    }

    private fun initView() {
        UserApiClient.instance.me { user, error ->
            val userId = user?.kakaoAccount?.profile?.nickname
            val str = userId + "님 안녕하세요"
            topView.text = str
        }

        accountBtn.setOnClickListener {
            val intent = Intent(this, AccountInfoActivity::class.java)
            startActivity(intent)
        }

        analyzeBtn.setOnClickListener {
            val intent = Intent(this, StockAnalyzeActivity::class.java)
            startActivity(intent)
        }

        recentAnalyzeButton.setOnClickListener {
            val intent = Intent(this, RecentAnalyzedActivity::class.java)
            startActivity(intent)
        }

        favStockSettingButton.setOnClickListener {
            val intent = Intent(this, FavoriteStockActivity::class.java)
            startActivityForResult(intent, REQUEST_LIST_STOCK)
        }

        newsButton.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initNaver() {
        NaverRepository.getSearchNews("주식", ::onSearchNewsFetched, ::onError)
    }


    private fun onSearchNewsFetched(news: List<Items>) {
        //Log.d("NaverAPI",news.toString())
        val newsView = findViewById<TextView>(R.id.newsView1)
        newsView1.text = news[0].title.parseAsHtml()
        newsView2.text = news[1].title.parseAsHtml()
        newsView3.text = news[2].title.parseAsHtml()
        newsView1.setOnClickListener {
            val intent = Intent(this, NewsDetailActivity::class.java)
            intent.putExtra("news_link", news[0].link)
            startActivity(intent)
        }
        newsView2.setOnClickListener {
            val intent = Intent(this, NewsDetailActivity::class.java)
            intent.putExtra("news_link", news[1].link)
            startActivity(intent)
        }
        newsView3.setOnClickListener {
            val intent = Intent(this, NewsDetailActivity::class.java)
            intent.putExtra("news_link", news[2].link)
            startActivity(intent)
        }
    }

    private fun onError() {
        Toast.makeText(this, "error result", Toast.LENGTH_SHORT).show()
    }

//    override fun onResume() {
//        super.onResume()
//        val retrofit = RetrofitClient.getInstance()
//        val myAPI = retrofit.create(IController::class.java)
//        UserApiClient.instance.me { user, error ->
//            myAPI.onLoad(user!!.id.toString()).enqueue(object : Callback<DonLoad> {
//                override fun onResponse(call: Call<DonLoad>, response: Response<DonLoad>) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onFailure(call: Call<DonLoad>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }
//    }
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//    if(resultCode != RESULT_OK) return
//
//    if(requestCode == REQUEST_LIST_STOCK){
//        if(data != null){
//            val receiveData = data.getStringArrayExtra("stock_list") as Array<String>
//            val str = "Code: " + receiveData!![0] + "   Price: " + receiveData!![1]
//            stock_view1.text = str
//        }
//    }
//}
}
