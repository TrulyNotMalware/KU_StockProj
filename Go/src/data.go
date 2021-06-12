package src

import (
	"database/sql"
)


type userStockData struct {

	code []string `json:"code"`
	date []string `json:"insertDate"`
	price []string `json:"realFirst"`
	predict []string `json:"predictLast"`
}

type userStockLog struct {

	code []string `json:"code"`
	analyzeDate []string `json:"ANALYZEDATE"`
	optionChoose []string `json:"OPTIONCHOOSE"`
}

func addStock(stockData *userStockData,queryData [4]sql.NullString){
	stockData.code=append(stockData.code, queryData[0].String)
	stockData.date=append(stockData.date, queryData[1].String)
	stockData.price=append(stockData.price, queryData[2].String)
	stockData.predict=append(stockData.predict, queryData[3].String)
}

func addStockLog(stockLog *userStockLog,queryData [3]sql.NullString){
	stockLog.code=append(stockLog.code, queryData[0].String)
	stockLog.analyzeDate=append(stockLog.analyzeDate, queryData[1].String)
	stockLog.optionChoose=append(stockLog.analyzeDate, queryData[2].String)
}
