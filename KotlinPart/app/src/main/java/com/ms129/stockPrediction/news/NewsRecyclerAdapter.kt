package com.ms129.stockPrediction.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.ms129.stockPrediction.R
import kotlinx.android.synthetic.main.news_item.view.*

class NewsRecyclerAdapter: RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    var itemClickListener:OnItemClickListener ?= null

    private var modelList = ArrayList<NewsData>()
    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val newsTitleView = itemView.news_title
        private val newsDateView = itemView.news_date
        init {
            itemView.setOnClickListener {
                itemClickListener!!.onItemClick(it, adapterPosition)
            }
        }

        fun bind(newsData: NewsData){
            newsTitleView.text = newsData.title?.parseAsHtml()
            newsDateView.text = newsData.date?.parseAsHtml()
        }

    }

    // 뷰홀더가 생성되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // 연결할 레이아웃 설정
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent,false))
    }
    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(this.modelList[position])
    }

    // 목록의 아이템수
    override fun getItemCount(): Int {
        return this.modelList.size
    }

    // 외부에서 데이터 넘기기
    fun submitList(modelList: ArrayList<NewsData>){
        this.modelList = modelList
    }
}