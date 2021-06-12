package com.ms129.stockPrediction.recentStock

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.ms129.stockPrediction.R
import com.ms129.stockPrediction.StockAnalyzeActivity
import com.ms129.stockPrediction.ms129Server.AnalyzedStock
import com.ms129.stockPrediction.ms129Server.DAnalyzeResult
import kotlinx.android.synthetic.main.activity_analyzed_detail.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class AnalyzedDetailActivity : AppCompatActivity() {
    lateinit var userId : String
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyzed_detail)
        val receivedIntent = intent
        val data = intent.getSerializableExtra("DATA") as DAnalyzeResult?
        val dataFromMain = intent.getSerializableExtra("MAIN_ANALYZED_DATA") as AnalyzedStock?
       // val id = intent.getStringExtra("ID") as String
      //  userId = id
        if(data != null){
            val stockData = AnalyzedStock(data.date, data.option, data.option2, data.resultData, data.realData, data.stockCode)
            initRealChart(stockData)
            initRecentChart(stockData)
            initText(stockData)
        }
        else{
            val stockData = AnalyzedStock(dataFromMain!!.date, dataFromMain.option, dataFromMain.option2
                , dataFromMain.resultData, dataFromMain.realData, dataFromMain.stockCode)
            initRealChart(stockData)
            initRecentChart(stockData)
            initText(stockData)
        }
    }

    private fun initText(stockData: AnalyzedStock) {
        analyzedDate.text = stockData.date.toString()
        analyzedStockCode.text = stockData.stockCode
        var optionText = ""
        when(stockData.option.toInt()){
            0 -> optionText = "1주 간격,"
            1 -> optionText = "2주 간격,"
            2 -> optionText = "1달 간격,"
            3 -> optionText = "2달 간격,"
            4 -> optionText = "3달 간격,"
        }
        when(stockData.option2.toInt()){
            0 -> optionText += " RNN"
            1 -> optionText += " LSTM"
            2 -> optionText += " CNN"
            3 -> optionText += " RNN + LSTM"
            4 -> optionText += " RNN + CNN"
            5 -> optionText += " LSTM + CNN"
            6 -> optionText += " RNN + CNN + LSTM"
        }
        analyzedOption.text = optionText
        var errorRate = calculateErrorRate(stockData.realData, stockData.resultData)
        var resultErrorRate = Math.round(errorRate * 100) / 100.0
        val result = "$resultErrorRate%"
        analyzedErrorRate.text = result
        analyzedErrorRate.setTextColor(Color.GREEN)
        setYields(stockData)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val intent = Intent(this, RecentAnalyzedActivity::class.java)
//        intent.putExtra("ID", userId)
//        startActivity(intent)
//        finish()
    }

    private fun setYields(stockData: AnalyzedStock){
        val realFirst = stockData.realData[0].toDouble()
        val realLast = stockData.realData[stockData.resultData.size - 1].toDouble()
        val predictLast = stockData.resultData[stockData.resultData.size - 1].toDouble()

        val yielddPredict = (predictLast - realFirst) / realFirst * 100
        var resultPredict = Math.round(yielddPredict * 100) / 100.0
        if(yielddPredict < 0){
            analyzedYieldRate.text = resultPredict.toString() + "%"
            analyzedYieldRate.setTextColor(Color.BLUE)
        }
        else{
            analyzedYieldRate.text = "+" + resultPredict.toString() + "%"
            analyzedYieldRate.setTextColor(Color.RED)
        }

        val yielddReal = (realLast - realFirst) / realFirst * 100
        var resultReal = Math.round(yielddReal * 100) / 100.0
        if(yielddReal < 0){
            realYieldRate.text = resultReal.toString() + "%"
            realYieldRate.setTextColor(Color.BLUE)
        }
        else{
            realYieldRate.text = "+" + resultReal.toString() + "%"
            realYieldRate.setTextColor(Color.RED)
        }
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
        val realSize = realData.size
        val resultSize = resultData.size
        val size = if(realSize > resultSize) resultSize
        else realSize
        for(i in 0 until size){
            total += ((real[i] - result[i])/real[i]).absoluteValue
        }
        return total / size
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
            description.isEnabled = false // 하단 Description Label 제거함
            invalidate() // refresh
        }

        // 범례
        chart.legend.apply {
            isEnabled = false // 사용하지 않음
        }
        // Y축
        chart.axisLeft.apply {
            // 라벨, 축라인, 그리드 사용하지 않음
            setDrawLabels(true)
            setDrawAxisLine(true)
            setDrawGridLines(true)
        }
        chart.axisRight.apply {
            // 우측 Y축은 사용하지 않음
            isEnabled = false
        }
        // X축
        chart.xAxis.apply {
            // x축 값은 투명으로
            setDrawLabels(false)
            textColor = Color.TRANSPARENT
            // 축라인, 그리드 사용하지 않음
            setDrawAxisLine(false)
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
//            textSize = 10f
//            granularity = 20f
//            axisMinimum = 2f
            isGranularityEnabled = true
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
//            addLimitLine(realAverageLine)
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
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }
    }
}