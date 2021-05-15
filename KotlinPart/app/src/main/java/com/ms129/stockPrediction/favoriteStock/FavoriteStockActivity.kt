package com.ms129.stockPrediction.favoriteStock

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ms129.stockPrediction.R

class FavoriteStockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_stock)
        init()
    }

    private fun init() {
        val addStockButton = findViewById<ImageButton>(R.id.addStockButton)
        addStockButton.setOnClickListener {
            val intent = Intent(this, EditFavoriteStock::class.java)
            startActivity(intent)
        }
    }


}
