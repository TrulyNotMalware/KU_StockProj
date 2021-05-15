package src

import (
	"database/sql"
)


type userStockData struct {
	stockList []string
}

func addStock(stockData *userStockData,queryData [1]sql.NullString){
	data :=queryData[0]
	stockData.stockList=append(stockData.stockList, data.String)
}