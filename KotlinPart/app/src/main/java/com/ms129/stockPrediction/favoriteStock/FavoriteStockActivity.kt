package com.ms129.stockPrediction.favoriteStock

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.ms129Server.IController
import com.ms129.stockPrediction.ms129Server.RetrofitClient
import kotlinx.android.synthetic.main.activity_favorite_stock.*

class FavoriteStockActivity : AppCompatActivity() {
    val REQUEST_CODE_STOCK = 100
    val REQUEST_LIST_STOCK = 99
    var receiveData = arrayOf("NONE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_stock)
        init()
    }

    private fun init() {
        val addStockButton = findViewById<ImageButton>(R.id.addStockButton)

        addStockButton.setOnClickListener {
            val intent = Intent(this, EditFavoriteStock::class.java)
            Log.d("Favorite::", "intent")
            startActivityForResult(intent, REQUEST_CODE_STOCK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityResult::", "PROGRESSING")
        if(resultCode != RESULT_OK) return

        if(requestCode == REQUEST_CODE_STOCK){
            if(data != null){
                Log.d("onActivityResult::", "SUCCESS")
                receiveData = data.getStringArrayExtra("jsonArray") as Array<String>
                //val favorite1 = findViewById<View>(R.id.favorite1)
                if(receiveData.size != 1){
                    Log.d("Data: ", receiveData!![0])
                    val str = "Code: " + receiveData!![0] + "   Price: " + receiveData!![1]
                    favorite1.text = str
                }
            }
        }
    }

    override fun onBackPressed() {
//        val array = receiveData
//        val returnIntent = Intent()
//        returnIntent.putExtra("stock_list", array)
//        setResult(RESULT_OK, returnIntent)
        finish()
    }

}
