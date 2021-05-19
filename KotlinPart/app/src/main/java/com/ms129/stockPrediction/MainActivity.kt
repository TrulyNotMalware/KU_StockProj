package com.ms129.stockPrediction

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.favoriteStock.FavoriteStockActivity
import com.ms129.stockPrediction.login.IController
import com.ms129.stockPrediction.login.RetrofitClient
import com.ms129.stockPrediction.naverAPI.INaverAPI
import com.ms129.stockPrediction.naverAPI.Items
import com.ms129.stockPrediction.naverAPI.NaverRepository
import com.ms129.stockPrediction.news.NewsActivity
import com.ms129.stockPrediction.news.NewsDetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var myAPI : IController
    lateinit var naverAPI : INaverAPI
    val REQUEST_LIST_STOCK = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(IController::class.java)
        init(myAPI)
        initSetting()
        initNaver()
        initView()
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
            val intent = Intent(this, StockAnalyzeAcitivy::class.java)
            startActivity(intent)
        }
    }

    private fun initSetting() {
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

    private fun init(myAPI: IController) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_OK) return

        if(requestCode == REQUEST_LIST_STOCK){
            if(data != null){
                val receiveData = data.getStringArrayExtra("stock_list") as Array<String>
                val str = "Code: " + receiveData!![0] + "   Price: " + receiveData!![1]
                stock_view1.text = str
            }
        }
    }
}
