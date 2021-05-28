package com.ms129.stockPrediction.recentStock

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms129.stockPrediction.R
import kotlinx.android.synthetic.main.activity_analyzed_detail.view.*
import kotlinx.android.synthetic.main.recent_stock_item.view.*

class RecentStockRecyclerAdapter:RecyclerView.Adapter<RecentStockRecyclerAdapter.RecentStockViewHolder>() {
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var itemClickListener:OnItemClickListener ?= null

    private var recentStockList = ArrayList<RecentStockData>()

    inner class RecentStockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val analyzedStockCodeView = itemView.recentStockCodeView
        private val analyzedStockDateView = itemView.recentDateView
        //private val analyzedOptionViewView = itemView.recent

        //        private val layoutExpand = itemView.findViewById<LinearLayout>(R.id.layout_expand)
        init {
            itemView.setOnClickListener {
                itemClickListener!!.onItemClick(it, adapterPosition)
            }
        }

        fun bind(recentStockData: RecentStockData){
            analyzedStockCodeView.text = recentStockData.stockCode
            analyzedStockDateView.text = recentStockData.date
            //analyzedOptionViewView.text = recentStockData.option
        }

    }

    // 뷰홀더가 생성되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentStockViewHolder {
        // 연결할 레이아웃 설정
        return RecentStockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recent_stock_item, parent,false))
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
    fun submitList(modelList: ArrayList<RecentStockData>){
        this.recentStockList = modelList
    }
}