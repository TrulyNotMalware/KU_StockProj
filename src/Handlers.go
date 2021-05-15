package src

import (
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"net/http"
)

type serverHandler struct {
	db       *sql.DB
	Strings  []string
	Sentence string
	userInfo map[string]string
	uid      string
}

func newServerHandler() *serverHandler {

	newHandler := &serverHandler{}
	newHandler.db,_=sql.Open("mysql","root:ms129@tcp(203.252.166.247:22)")
	newHandler.Strings = []string{"test1", "test2", "test3"}
	newHandler.Sentence = "test1"
	newHandler.uid = "1"
	newHandler.userInfo = make(map[string]string)
	newHandler.userInfo["UID"] = "1"
	newHandler.userInfo["UNAME"] = "kim"
	newHandler.userInfo["PROFILELINK"] = "naver"
	return newHandler
}

func (s *serverHandler) loginHandler(res http.ResponseWriter, req *http.Request) {
	// update person information
	req.ParseForm()
	fmt.Println("form req log")
	fmt.Println(req.Form)
}

func (s *serverHandler) testHandler(res http.ResponseWriter, req *http.Request) {
	//
	if req.Method == "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "OnlyForPost", errors.New("Req.method is not Post"))
		return
	}
	req.ParseForm()
	fmt.Println(req.Form)
}

func (s *serverHandler) aiHandler(res http.ResponseWriter, req *http.Request) {
	// send information to python
	req.ParseForm()
	fmt.Println("form req log")
	fmt.Println(req.Form)
}

func (s *serverHandler) logHandler(res http.ResponseWriter, req *http.Request) {
	// load user log

}

func (s *serverHandler) onLoadHandler(res http.ResponseWriter, req *http.Request) {
	// load user information from database
	if req.Method != "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "OnlyForGet", errors.New("Request method is Post"))
		return
	}

	key := []string{"UID"}
	value := make([]string, len(key))\
	if readFromRequest(&res, req, key, &value) == false {
		return
	}
	//쿼리문 - request로 받음 uid 이용하여 관심 종목 db에서 꺼내오기
	//requset로 부터 uid parsing
	userId := value[0]
	queryData, err := s.db.Query("select STOCKNAME from USERSTOCK WHERE UID=(?)",userId)
	if err != nil {
		errorHandling(&res, 500, "DB error", err)
	}

	var queryString [1]sql.NullString
	var stockData userStockData

	for queryData.Next(){
		err=queryData.Scan(&queryString[0])
		if err!=nil{
			errorHandling(&res, 500, "parsing erro", err)
		}
		addStock(&stockData,queryString)
	}
	sendData,err:=json.Marshal(stockData)
	res.WriteHeader(200)
	res.Write(sendData)

}
