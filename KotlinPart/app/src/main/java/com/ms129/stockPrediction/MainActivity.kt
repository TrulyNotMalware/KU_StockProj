package com.ms129.stockPrediction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.LinearLayoutManager
import com.ms129.stockPrediction.favoriteStock.FavoriteStockActivity
import com.ms129.stockPrediction.ms129Server.*
import com.ms129.stockPrediction.naverAPI.INaverAPI
import com.ms129.stockPrediction.naverAPI.Items
import com.ms129.stockPrediction.naverAPI.NaverRepository
import com.ms129.stockPrediction.news.NewsActivity
import com.ms129.stockPrediction.news.NewsDetailActivity
import com.ms129.stockPrediction.recentStock.AnalyzedDetailActivity
import com.ms129.stockPrediction.recentStock.RecentAnalyzedActivity
import com.ms129.stockPrediction.recentStock.AnalyzedRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var myAPI : IController
    lateinit var naverAPI : INaverAPI
    lateinit var favoriteRecyclerAdapter : FavoriteRecyclerAdapter
    lateinit var analyzedRecyclerAdapter: AnalyzedRecyclerAdapter
    val REQUEST_LIST_STOCK = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(IController::class.java)
        init()

    }


    private fun init() {
        val receivedIntent = intent
        val isFirst = receivedIntent.getStringExtra("IS_FIRST") as String
        val nickName = receivedIntent.getStringExtra("NICK_NAME") as String
        val id = receivedIntent.getStringExtra("ID") as String

        initNaver()

        setButtonListener()
        if(isFirst == "0"){
            myAPI.onLoad(id).enqueue(object: Callback<DonLoad?> {
                override fun onResponse(call: Call<DonLoad?>, response: Response<DonLoad?>) {
                    Log.d("[MAIN] OnLoad Success::", response.body().toString())
                    val onLoadData = response.body()
                    initTopView(onLoadData?.userName)
                    initFavoriteSector(onLoadData?.favoriteStocks)
                    initAnalyzedSector(onLoadData?.analyzedStocks)
                }
                override fun onFailure(call: Call<DonLoad?>, t: Throwable) {
                    Log.d("[MAIN] OnLoad Failure::", t.toString())
                    initTopView("FAIL")
                    initFavoriteSector(listOf(FavoriteStock("FAIL", "FAIL", "FAIL")))
                    initAnalyzedSector(listOf(AnalyzedStock("nullOrSize0", "nullOrSize0", listOf("0", "1"), listOf("0", "1"),"nullOrSize0")))
                }
            })
        }
        else{
            Log.d("[MAIN] isFirst::", "YES")
            initTopView("FAIL")
            initFavoriteSector(listOf(FavoriteStock("FAIL", "FAIL", "FAIL")))
            initAnalyzedSector(listOf(AnalyzedStock("nullOrSize0", "nullOrSize0", listOf("0", "1"), listOf("0", "1"),"nullOrSize0")))
        }
    }

    private fun initAnalyzedSector(analyzedStocks: List<AnalyzedStock>?) {
        analyzedRecyclerAdapter = AnalyzedRecyclerAdapter()
        val temp = ArrayList<AnalyzedStock>()
        var modelList = ArrayList<AnalyzedStock>()
        if(analyzedStocks == null || analyzedStocks.isEmpty()){
            Log.e("[MAIN] AnalyzedSector::", "null or empty")
            val data1 = AnalyzedStock("nullOrSize0", "nullOrSize0", listOf("0", "1"), listOf("0", "1"),"nullOrSize0")
            modelList.add(data1)
            analyzedRecyclerAdapter.submitList(modelList)
        }
        else{
            Log.e("[MAIN] AnalyzedSector::", ">=5")
            if(analyzedStocks.size >= 3)
                for(i in 0..2)
                    temp.add(analyzedStocks[i])
            else
                for(stock in analyzedStocks)
                    temp.add(stock)
            modelList = temp
            analyzedRecyclerAdapter.submitList(modelList)
        }
        analyzedRecyclerAdapter.itemClickListener = object: AnalyzedRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, AnalyzedDetailActivity::class.java)
                intent.putExtra("MAIN_ANALYZED_DATA", temp[position])
                startActivity(intent)
            }
        }
        analyzedMainRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        analyzedMainRecyclerView.adapter = analyzedRecyclerAdapter
    }

    private fun initFavoriteSector(favoriteStocks: List<FavoriteStock>?) {
        favoriteRecyclerAdapter = FavoriteRecyclerAdapter()
        var modelList = ArrayList<FavoriteStock>()
        if(favoriteStocks == null || favoriteStocks.isEmpty()){
            val data1 = FavoriteStock("nullOrSize0", "nullOrSize0", "nullOrSize0")
            modelList.add(data1)
            favoriteRecyclerAdapter.submitList(modelList)
        }
        else{
            val temp = ArrayList<FavoriteStock>()
            Log.e("[MAIN] FavoriteSector::", ">=5")
            if(favoriteStocks.size >= 5)
                for(i in 0..5)
                    temp.add(favoriteStocks[i])
            else
                for(stock in favoriteStocks)
                    temp.add(stock)
            modelList = temp
            favoriteRecyclerAdapter.submitList(modelList)
        }
        favoriteRecyclerAdapter.itemClickListener = object: FavoriteRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {

            }
        }
        interestRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        interestRecyclerView.adapter = favoriteRecyclerAdapter
    }

    private fun setButtonListener(){
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

    private fun initTopView(userId: String?) {
        if(userId == null){
            val str = "NULLLLLL님 안녕하세요"
            topView.text = str
        }
            val str = userId + "님 안녕하세요"
            topView.text = str
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
