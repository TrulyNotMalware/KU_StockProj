package com.ms129.stockPrediction

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.recentStock.RecentStockData
import kotlinx.android.synthetic.main.activity_analyzed_detail.view.*
import kotlinx.android.synthetic.main.recent_stock_item.view.*
import kotlin.math.roundToInt

class FavoriteRecyclerAdapter:RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteStockViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var itemClickListener:OnItemClickListener ?= null

    private var recentStockList = ArrayList<RecentStockData>()

    inner class FavoriteStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val favoriteStockCode = itemView.recentStockCodeView
        private val favoriteStockDateView = itemView.recentDateView
        private val favoritePredictView = itemView.recentPredictPriceView
        private val favoriteLastPriceView = itemView.recentLastPriceView
        private val favoriteYieldView = itemView.recentYieldView
        //private val analyzedOptionViewView = itemView.recent

        //        private val layoutExpand = itemView.findViewById<LinearLayout>(R.id.layout_expand)
        init {
            itemView.setOnClickListener {
                itemClickListener!!.onItemClick(it, adapterPosition)
            }
        }

        fun bind(recentStockData: RecentStockData){
            favoriteStockCode.text = recentStockData.stockCode
            favoriteStockDateView.text = recentStockData.date
            favoritePredictView.text = recentStockData.resultData[0]
            favoriteLastPriceView.text = recentStockData.realData[0]
            var yieldd = (recentStockData.realData[0].toDouble() - recentStockData.resultData[0].toDouble()) / recentStockData.resultData[0].toDouble()
            yieldd = ((yieldd * 100).roundToInt() / 10f).toDouble()
            if(yieldd < 0)
                favoriteYieldView.text = yieldd.toString() + "%"
            else
                favoriteYieldView.text = "+" + yieldd.toString() + "%"
            //analyzedOptionViewView.text = recentStockData.option
        }

    }

    // 뷰홀더가 생성되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStockViewHolder {
        // 연결할 레이아웃 설정
        return FavoriteStockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recent_stock_item, parent,false))
    }
    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: FavoriteStockViewHolder, position: Int) {
        holder.bind(this.recentStockList[position])
    }

    // 목록의 아이템수
    override fun getItemCount(): Int {
        return this.recentStockList.size
    }

    // 외부에서 데이터 넘기기
    fun submitList(modelList: ArrayList<RecentStockData>){
        this.recentStockList = modelList
    }
}