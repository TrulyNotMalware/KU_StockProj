package com.ms129.stockPrediction


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.ms129Server.*
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var myAPI : IController
    val userApiClient = UserApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash: ", keyHash)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
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

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()


            }
        }

        kakao_login_button.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun requestLogin() {
//        myAPI.login(loginBody).enqueue(object: Callback<LoginReturnData> {
//            override fun onResponse(call: Call<LoginReturnData>, response: Response<LoginReturnData>) {
//                if (!response.isSuccessful) {
//                    Log.d("KAKAO Login isn't Successful::", response.body().toString())
//                    return
//                }
//                Log.d("KAKAO Login Success::", response.body().toString())
////                if(response.body().toString() == "0"){ // 처음이 아님
//                    dataLoad("1")
////                }
////                else{
////                    goToMainActivity()
////                }
//            }
//            override fun onFailure(call: Call<LoginReturnData>, t: Throwable) {
//                Log.d("KAKAO Login Failure::", t.toString())
//                goToMainActivity()
//            }
//        })
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
                        if(result == "0"){ // 처음이 아님
                            dataLoad(userId)
                        }
                        else{
                            goToMainActivity()
                        }
                    }
                    override fun onFailure(call: Call<LoginReturnData>, t: Throwable) {
                        Log.d("KAKAO Login Failure::", t.toString())
                        Toast.makeText(this@LoginActivity, "로그인 실패 에러", Toast.LENGTH_LONG).show()
                        goToMainActivity()
                    }
                })
            }
            else{
                Log.e("UserApiClient-user::userInfo[0]", "ERROR")
            }
        }
    }

    private fun dataLoad(id: String) {
        myAPI.onLoad(id).enqueue(object: Callback<DonLoad?> {
            override fun onResponse(call: Call<DonLoad?>, response: Response<DonLoad?>) {
                Log.d("KAKAO OnLoad Success::", response.body().toString())
                Log.d("KAKAO OnLoad Success2::", response.body()!!.analyzedStocks[0].date)
                //val str = response.body()?.code?.get(0)!!
                //Log.d("KAKAO OnLoad Success2::", str)
            }
            override fun onFailure(call: Call<DonLoad?>, t: Throwable) {
                Log.d("KAKAO OnLoad Failure::", t.toString())
            }

        })
        goToMainActivity()

    }

    private fun goToMainActivity(){
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}
