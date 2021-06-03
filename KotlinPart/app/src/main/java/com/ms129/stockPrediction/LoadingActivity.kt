package com.ms129.stockPrediction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.ms129Server.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadingActivity : AppCompatActivity() {
    lateinit var myAPI : IController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val retrofit = RetrofitClient.getInstance()
                myAPI = retrofit.create(IController::class.java)
                requestLogin()

//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
    }

    private fun requestLogin() {
        UserApiClient.instance.me { user, error ->
            if(user != null){
                val userId = user.id.toString()
                val nickName = user.kakaoAccount?.profile?.nickname.toString()
                val profileImageLink = user.kakaoAccount?.profile?.profileImageUrl.toString()
                val loginBody = LoginBody(userId, nickName, profileImageLink)
                Log.d("KAKAO:instanceInfo->", "$userId, $nickName, $profileImageLink")

                myAPI.login(loginBody).enqueue(object: Callback<LoginReturnData> {
                    override fun onResponse(call: Call<LoginReturnData>, response: Response<LoginReturnData>) {
                        if (!response.isSuccessful) {
                            Log.d("KAKAO Login isn't Successful::", response.body().toString())
                            return
                        }
                        Log.d("KAKAO Login Success::", response.body()!!.isFirst)
                        val result = response.body()!!.isFirst
                        // result : 0 처음 아님 1: 처음
                        goToMainActivity(result)
                    }
                    override fun onFailure(call: Call<LoginReturnData>, t: Throwable) {
                        Log.d("KAKAO Login Failure::", t.toString())
                        Toast.makeText(this@LoadingActivity, "로그인 실패 에러", Toast.LENGTH_LONG).show()
                        goToMainActivity("999")
                    }
                })
            }
            else{
                Log.e("UserApiClient-user::userInfo[0]", "ERROR")
            }
        }
    }

//    private fun dataLoad(id: String) {
//        myAPI.onLoad(id).enqueue(object: Callback<DonLoad?> {
//            override fun onResponse(call: Call<DonLoad?>, response: Response<DonLoad?>) {
//                Log.d("KAKAO OnLoad Success::", response.body().toString())
//                //Log.d("KAKAO OnLoad Success2::", response.body()!!.analyzedStocks[0].date)
//                //val str = response.body()?.code?.get(0)!!
//                //Log.d("KAKAO OnLoad Success2::", str)
//                goToMainActivity(response.body()!!)
//            }
//            override fun onFailure(call: Call<DonLoad?>, t: Throwable) {
//                Log.d("KAKAO OnLoad Failure::", t.toString())
//                goToMainActivity()
//            }
//        })
//
//
//    }

//    private fun goToMainActivity(onLoadData: DonLoad){
//        val intent = Intent(baseContext, MainActivity::class.java)
//        intent.putExtra("ON_LOAD_DATA", onLoadData)
//        startActivity(intent)
//        finish()
//    }

    private fun goToMainActivity(isFirst : String) {
        val intent = Intent(baseContext, MainActivity::class.java)
        if(isFirst == "9990"){
            intent.putExtra("ID", "FAIL")
            intent.putExtra("NICK_NAME", "FAIL")
            intent.putExtra("IS_FIRST", "1")
            startActivity(intent)
            finish()
        }
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                val userId = user.id.toString()
                val nickName = user.kakaoAccount?.profile?.nickname.toString()
                intent.putExtra("ID", userId)
                intent.putExtra("NICK_NAME", nickName)
                intent.putExtra("IS_FIRST", isFirst)
                startActivity(intent)
                finish()
            }
            else{
                Log.e("goToMainActivity", "user != null")
                intent.putExtra("ID", "sample")
                intent.putExtra("NICK_NAME", "sample")
                intent.putExtra("IS_FIRST", "1")
                startActivity(intent)
                finish()
            }
        }
    }
}
