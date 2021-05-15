package com.ms129.stockPrediction

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.favoriteStock.FavoriteStockActivity
import com.ms129.stockPrediction.naverAPI.INaverAPI
import com.ms129.stockPrediction.naverAPI.Items
import com.ms129.stockPrediction.naverAPI.NaverRepository
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var myAPI : IController
    lateinit var naverAPI : INaverAPI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)

//        UserApiClient.instance.me { user, error ->
//            id.text = "회원번호: ${user?.id}"
//            nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
//            profileimage_url.text = "프로필 링크: ${user?.kakaoAccount?.profile?.profileImageUrl}"
//        }

        val retrofit = RetrofitClient.getInstance()
        myAPI = retrofit.create(IController::class.java)
        //init(myAPI)
        initSetting()
        initNaver()

    }

    private fun initSetting() {
        val editFavoriteView = findViewById<View>(R.id.editFavoriteView)
        editFavoriteView.setOnClickListener {
            val intent = Intent(this, FavoriteStockActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initNaver() {
        NaverRepository.getSearchNews("주식", ::onSearchNewsFetched, ::onError)
    }

    private fun onSearchNewsFetched(news: List<Items>) {
        //Log.d("NaverAPI",news.toString())
        val newsView = findViewById<TextView>(R.id.newsView)
        newsView.text = news[0].title.parseAsHtml()
    }

    private fun onError() {
        Toast.makeText(this, "error result", Toast.LENGTH_SHORT).show()
    }

    private fun init(myAPI: IController) {

        kakao_logout_button.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
            }

        }

        kakao_unlink_button.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
        }

        button.setOnClickListener {
            val post = DataClass(1, "a", "b")

            myAPI.test(post)?.enqueue(object : Callback<DataClass?>{
                override fun onResponse(call: Call<DataClass?>, response: Response<DataClass?>) {
                    Log.d("Response:: ", response.body().toString())
                    val id = response.body()!!.userId
                    Log.d("${id}:: ", id.toString())
                    Log.d("onResponse::", "Success")
                }

                override fun onFailure(call: Call<DataClass?>, t: Throwable) {
                    Log.d("myAPI::", "Failed API call with call: " + call +
                            " + exception: " + t)
                }
            })

            val result = myAPI.getTest()
            result?.enqueue(object: Callback<String?>{
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    Log.d("onResponse:: ", "Success : $response")
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Log.d("onFailure:: ", "exception: $t")
                }

            })
        }
    }
}
