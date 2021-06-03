package com.ms129.stockPrediction.recentStock

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.ms129Server.AnalyzedStock
import com.ms129.stockPrediction.ms129Server.DAnalyzeResult
import kotlinx.android.synthetic.main.activity_analyzed_detail.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class AnalyzedDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyzed_detail)
        val receivedIntent = intent
        val data = intent.getSerializableExtra("DATA") as DAnalyzeResult?
        val dataFromMain = intent.getSerializableExtra("MAIN_ANALYZED_DATA") as AnalyzedStock?
        if(data != null){
            val stockData = AnalyzedStock(data.date, data.option, data.resultData, data.realData, data.stockCode)
            initRealChart(stockData)
            initRecentChart(stockData)
            initText(stockData)
        }
        else{
            val stockData = AnalyzedStock(dataFromMain!!.date, dataFromMain.option
                , dataFromMain.resultData, dataFromMain.realData, dataFromMain.stockCode)
            initRealChart(stockData)
            initRecentChart(stockData)
            initText(stockData)
        }
    }

    private fun initText(stockData: AnalyzedStock) {
        analyzedDate.text = stockData.date.toString()
        analyzedStockCode.text = stockData.stockCode
        when(stockData.option.toInt()){
            0 -> analyzedOption.text = "1주 간격 머신러닝"
            1 -> analyzedOption.text = "2주 간격 머신러닝"
            2 -> analyzedOption.text = "1달 간격 머신러닝"
            3 -> analyzedOption.text = "2달 간격 머신러닝"
            4 -> analyzedOption.text = "3달 간격 머신러닝"
        }
        var errorRate = calculateErrorRate(stockData.realData, stockData.resultData)
        errorRate = ((errorRate*100).roundToInt() / 10f).toDouble()
        val result = "$errorRate%"
        analyzedErrorRate.text = result
    }

    private fun calculateErrorRate(
        realData: List<String>,
        resultData: List<String>
    ): Double {
        var real = ArrayList<Double>()
        var result = ArrayList<Double>()
        for(data in realData)
            real.add(data.toDouble())
        for(data in resultData)
            result.add(data.toDouble())

        var total = 0.0
        for(i in 0..realData.size){
            total += ((real[0] - result[0])/real[0]).absoluteValue
        }
        return total / realData.size
    }

    fun initRecentChart(stockData : AnalyzedStock){
        // 평균 값 구하기
        var average = 0F
        for(stock in stockData.resultData){
            average += stock.toFloat()
        }
//        for(stock in DataUtil.getStockData()) {
//            average += stock.price.toFloat()
//        }
        //average /= DataUtil.getStockData().size
        average /= stockData.resultData.size

        // 그래프에 들어갈 데이터 준비
        val entries = ArrayList<Entry>()
//        for (stock in DataUtil.getStockData()) {
//            entries.add(Entry(stock.index.toFloat(), stock.price.toFloat()))
//        }
        var i = 1
        for(stock in stockData.resultData){
            entries.add(Entry(i.toFloat(), stock.toFloat()))
            i += 1
        }

        val dataSet = LineDataSet(entries, "").apply {
            setDrawCircles(false)
            color = Color.RED
            highLightColor = Color.TRANSPARENT
            valueTextSize = 0F
            lineWidth = 1.5F
        }

        val lineData = LineData(dataSet)
        chart.run {
            data = lineData
            description.isEnabled = true // 하단 Description Label 제거함
            description.text = "날짜"
            description.textSize = 15f
            invalidate() // refresh
        }

        val averageLine = LimitLine(average).apply {
            lineWidth = 1F
            enableDashedLine(4F, 10F, 10F)
            lineColor = Color.DKGRAY
        }

        // 범례
        chart.legend.apply {
            isEnabled = false // 사용하지 않음
        }
        // Y축
        chart.axisLeft.apply {
            // 라벨, 축라인, 그리드 사용하지 않음
            setDrawLabels(false)
            setDrawAxisLine(true)
            setDrawGridLines(false)

            // 한계선 추가
            removeAllLimitLines()
            addLimitLine(averageLine)
        }
        chart.axisRight.apply {
            // 우측 Y축은 사용하지 않음
            isEnabled = true
        }
        // X축
        realChart.xAxis.apply {
            // x축 값은 투명으로
            textColor = Color.TRANSPARENT
            // 축라인, 그리드 사용하지 않음
            setDrawAxisLine(true)
            setDrawGridLines(false)
        }
    }

    fun initRealChart(stockData : AnalyzedStock){
        //-----------실제 데이터--------------//

        // 평균 값 구하기
        var realAverage = 0F
        for(stock in stockData.realData){
            realAverage += stock.toFloat()
        }
//        for(stock in DataUtil.getStockData()) {
//            average += stock.price.toFloat()
//        }
        //average /= DataUtil.getStockData().size
        realAverage /= stockData.realData.size

        // 그래프에 들어갈 데이터 준비
        val realEntries = ArrayList<Entry>()
        var i = 1
        for(stock in stockData.realData){
            realEntries.add(Entry(i.toFloat(), stock.toFloat()))
            i += 1
        }

        val realDataSet = LineDataSet(realEntries, "").apply {
            setDrawCircles(false)
            color = Color.BLUE
            highLightColor = Color.TRANSPARENT
            valueTextSize = 0F
            lineWidth = 1.5F
        }

        val realLineData = LineData(realDataSet)
        realChart.run {
            data = realLineData
            description.isEnabled = false // 하단 Description Label 제거함
            invalidate() // refresh
        }

        val realAverageLine = LimitLine(realAverage).apply {
            lineWidth = 1F
            enableDashedLine(4F, 10F, 10F)
            lineColor = Color.DKGRAY
        }

        // 범례
        realChart.legend.apply {
            isEnabled = false // 사용하지 않음
        }

        // y축
        realChart.axisLeft.apply {
            // 라벨, 축라인, 그리드 사용하지 않음
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)

            // 한계선 추가
            removeAllLimitLines()
            addLimitLine(realAverageLine)
        }
        realChart.axisRight.apply {
            // 우측 Y축은 사용하지 않음
            isEnabled = false
        }

        // x축
        realChart.xAxis.apply {
            // x축 값은 투명으로
            textColor = Color.TRANSPARENT
            // 축라인, 그리드 사용하지 않음
            setDrawAxisLine(true)
            setDrawGridLines(true)
        }
    }
}