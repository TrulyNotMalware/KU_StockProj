package com.ms129.stockPrediction.recentStock

import android.graphics.Color
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
        private val analyzedModelView = itemView.analyzedModelView
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
            var realFirst = analyzedStock.realData[0].toDouble()
            var predictLast = analyzedStock.resultData[analyzedStock.resultData.size - 1].toDouble()
            val predictResult = Math.round(predictLast * 100) / 100.0
            analyzedPredictView.text = "$" + predictResult.toString()
            var option1 = ""
            var option2 = ""
            when(analyzedStock.option){
                "0"-> option1 = "1주 간격"
                "1"-> option1 = "2주 간격"
                "2"-> option1 = "1달 간격"
                "3"-> option1 = "2달 간격"
                "4"-> option1 = "3달 간격"
            }
            when(analyzedStock.option2){
                "0"-> option2 = "RNN"
                "1"-> option2 = "LSTM"
                "2"-> option2 = "CNN"
                "3"-> option2 = "R + M"
                "4"-> option2 = "R + N"
                "5"-> option2 = "C + L"
                "6"-> option2 = "R+C+L"
            }
            analyzedModelView.text = option2.toString()
            var yieldd = (predictLast - realFirst) / realFirst * 100
            var result = Math.round(yieldd * 100) / 100.0
            if(yieldd < 0){
                analyzedYieldView.text = result.toString() + "%"
                analyzedYieldView.setTextColor(Color.BLUE)
            }
            else{
                analyzedYieldView.text = "+" + result.toString() + "%"
                analyzedYieldView.setTextColor(Color.RED)
            }
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