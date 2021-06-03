package com.ms129.stockPrediction.recentStock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.ms129Server.AnalyzedStock
import kotlinx.android.synthetic.main.analyzed_stock_item.view.*

class AnalyzedRecyclerAdapter:RecyclerView.Adapter<AnalyzedRecyclerAdapter.RecentStockViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var itemClickListener:OnItemClickListener ?= null

    private var recentStockList = ArrayList<AnalyzedStock>()

    inner class RecentStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val analyzedStockCodeView = itemView.analyzedStockCodeView
        private val analyzedStockDateView = itemView.analyzedDateView
        private val analyzedPredictView = itemView.analyzedPredictPriceView
        private val analyzedYieldView = itemView.analyzedYieldView
        //private val analyzedOptionViewView = itemView.recent

        //        private val layoutExpand = itemView.findViewById<LinearLayout>(R.id.layout_expand)
        init {
            itemView.setOnClickListener {
                itemClickListener!!.onItemClick(it, adapterPosition)
            }
        }

        fun bind(analyzedStock: AnalyzedStock){
            analyzedStockCodeView.text = analyzedStock.stockCode
            analyzedStockDateView.text = analyzedStock.date
            analyzedPredictView.text = "test"
            var yieldd = 99.99
            if(yieldd < 0)
                analyzedYieldView.text = yieldd.toString() + "%"
            else
                analyzedYieldView.text = "+" + yieldd.toString() + "%"
            //analyzedOptionViewView.text = recentStockData.option
            //analyzedOptionViewView.text = recentStockData.option
        }

    }

    // 뷰홀더가 생성되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentStockViewHolder {
        // 연결할 레이아웃 설정
        return RecentStockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.analyzed_stock_item, parent,false))
    }
    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: RecentStockViewHolder, position: Int) {
        holder.bind(this.recentStockList[position])
    }

    // 목록의 아이템수
    override fun getItemCount(): Int {
        return this.recentStockList.size
    }

    // 외부에서 데이터 넘기기
    fun submitList(modelList: ArrayList<AnalyzedStock>){
        this.recentStockList = modelList
    }
}