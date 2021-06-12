package com.ms129.stockPrediction

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms129.stockPrediction.ms129Server.FavoriteStock
import kotlinx.android.synthetic.main.analyzed_stock_item.view.*
import kotlinx.android.synthetic.main.favorite_stock_item.view.*

class FavoriteRecyclerAdapter:RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteStockViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var itemClickListener:OnItemClickListener ?= null

    private var favoriteStockList = ArrayList<FavoriteStock>()

    inner class FavoriteStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val favoriteStockCode = itemView.favoriteStockCodeView
        private val favoriteStockDateView = itemView.favoriteDateView
        private val favoriteLastPriceView = itemView.favoriteLastPriceView
        private val favoriteYieldView = itemView.favoriteYieldView
        private val favoritePredictPriceView = itemView.favoritePredictPriceView

        init {
            itemView.setOnClickListener {
                itemClickListener!!.onItemClick(it, adapterPosition)
            }
        }

        fun bind(favoriteStock: FavoriteStock){
            favoriteStockCode.text = favoriteStock.stockCode
            favoriteStockDateView.text = favoriteStock.date
            val firstResult  = Math.round(favoriteStock.realFirst.toDouble() * 100) / 100.0
            val lastResult = Math.round(favoriteStock.predictLast.toDouble() * 100) / 100.0
            favoriteLastPriceView.text = "$" + firstResult
            favoritePredictPriceView.text = "$" + lastResult
            var yieldd = (favoriteStock.predictLast.toDouble() - favoriteStock.realFirst.toDouble()) / favoriteStock.realFirst.toDouble() * 100
            var result = Math.round(yieldd * 100) / 100.0
            if(yieldd < 0) {
                favoriteYieldView.text = result.toString() + "%"
                favoriteYieldView.setTextColor(Color.BLUE)
            }
            else {
                favoriteYieldView.text = "+" + result.toString() + "%"
                favoriteYieldView.setTextColor(Color.RED)
            }
            //analyzedOptionViewView.text = recentStockData.option
        }

    }

    // 뷰홀더가 생성되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStockViewHolder {
        // 연결할 레이아웃 설정
        return FavoriteStockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_stock_item, parent,false))
    }
    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: FavoriteStockViewHolder, position: Int) {
        holder.bind(this.favoriteStockList[position])
    }

    // 목록의 아이템수
    override fun getItemCount(): Int {
        return this.favoriteStockList.size
    }

    // 외부에서 데이터 넘기기
    fun submitList(modelList: ArrayList<FavoriteStock>){
        this.favoriteStockList = modelList
    }
}