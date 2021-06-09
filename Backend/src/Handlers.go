package src

import (
	"database/sql"
	"errors"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"log"
	"net/http"
	"os"
	"strings"
	"io/ioutil"
)

type serverHandler struct {
	db       *sql.DB
	Strings  []string
	Sentence string
	userInfo map[string]string
	uid      string
	aiUnit   *aiControl
}

func newServerHandler() *serverHandler {

	newHandler := &serverHandler{}
	newHandler.db, _ = sql.Open("mysql", "root:1234@tcp(localhost)/userinfo")
	newHandler.userInfo = make(map[string]string)
	newHandler.aiUnit = newAIControlBlock()

	return newHandler
}

func (s *serverHandler) loginHandler(res http.ResponseWriter, req *http.Request) {
	if req.Method == "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "Only For Post.", errors.New("Req.Method is not Post."))
		return
	}

	key := []string{"UID", "UNAME", "PROFILELINK"}
	value := make([]string, len(key))
	if readFromRequest(&res, req, key, &value) == false {
		return
	}

	userId := value[0]
	userName := value[1]
	profileLink := value[2]
	queryData, err := s.db.Query("select UID from USER where UID=?", userId)
	if err != nil {
		errorHandling(&res, 500, "DB error", err)
		return
	}
	isFirst := "1"
	var queryString [1]sql.NullString
	for queryData.Next() {
		err = queryData.Scan(&queryString[0])
		if err != nil {
			errorHandling(&res, 500, "parsing error", err)
			return
		}
		isFirst = "0"
	}
	if isFirst == "1" {
		result, err := s.db.Exec("INSERT INTO USER(UID,UNAME,PROFILELINK) values (?,?,?)", userId, userName, profileLink)
		if err != nil {
			fmt.Print("Query Error.")
			errorHandling(&res, 500, "DB Insert Error.", err)
			return
		}
		nRow, err := result.RowsAffected()
		fmt.Println("Query Ok. Inserted Count :", nRow)
	}
	res.WriteHeader(200)
	res.Write([]byte("{\"isFirst\": \"" + isFirst + "\"}"))
	fmt.Println("Res Ok. UID :" + userId + ", UNAME:" + userName + ", PROFILELINK:" + profileLink)

}

func (s *serverHandler) interestAddHandler(res http.ResponseWriter, req *http.Request) {
	// add interest stock
	if req.Method == "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "Only For Post.", errors.New("Req.Method is not GET."))
		return
	}

	key := []string{"UID", "stockCode", "date", "price"}
	value := make([]string, len(key))

	if readFromRequest(&res, req, key, &value) == false {
		return
	}
	fmt.Println(value)

	s.aiUnit.publishQueue( value[1] + "_0_0_"+value[0])
	fmt.Println(value[1] + "_0_0_"+value[0])
	receiveVal := s.aiUnit.consumeQueue()
	fmt.Println(receiveVal)
	data := strings.Split(receiveVal, "$")
	predictString := data[0]
	predictList := strings.Split(predictString, ",")
	predictData := predictList[len(predictList)-2]
	actualString := data[1]
	actualList := strings.Split(actualString, ",")
	actualData := actualList[1]

	result, err := s.db.Exec("INSERT INTO USERSTOCK(UID,CODE,INSERTDATE,REALFIRST,PREDICTPRICE) values (?,?,?,?,?)", value[0], value[1], value[2], actualData, predictData)
	if err != nil {
		fmt.Print("Query Error.")
		errorHandling(&res, 500, "DB Insert Error.", err)
		return
	}
	nRow, err := result.RowsAffected()
	fmt.Println("Query Ok. Inserted Count :", nRow)
	res.WriteHeader(200)
	res.Write([]byte("{\"predictLast\" : \""+predictData+"\",\n"))
	res.Write([]byte("\"realFirst\" : \""+actualData+"\"}"))
}

func (s *serverHandler) interestDeleteHandler(res http.ResponseWriter, req *http.Request) {
	// delete interest stock   method - post
	if req.Method == "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "Only For Post.", errors.New("Req.Method is not GET."))
		return
	}

	key := []string{"UID", "stockCode"}
	value := make([]string, len(key))
	if readFromRequest(&res, req, key, &value) == false {
		return
	}
	fmt.Println(value)

	result, err := s.db.Exec("DELETE from USERSTOCK where UID=? and CODE=?", value[0], value[1])
	if err != nil {
		fmt.Print("Query Error.")
		errorHandling(&res, 500, "DB Delete Error.", err)
		return
	}
	nRow, err := result.RowsAffected()
	fmt.Println("Query Ok. Deleted Count :", nRow)

	res.WriteHeader(200)
	res.Write([]byte("{\"Delete\" : \"1\"}"))

}

func (s *serverHandler) analyzeHandler(res http.ResponseWriter, req *http.Request) {
	// method post
	if req.Method == "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "Only For Post.", errors.New("Req.Method is not GET."))
		return
	}

	key := []string{"UID", "stockCode", "date", "option","option2"} //option : learning term, option2 : model
	value := make([]string, len(key))
	if readFromRequest(&res, req, key, &value) == false {
		return
	}
	valueString := value[0] + "_" + value[1] + "_" + value[2] + "_" + value[3]+ "_" + value[4]+".txt"
	fmt.Println(valueString)

	targetDir := "./LogData"
	files, err := ioutil.ReadDir(targetDir)
	if err != nil {
		return
	}
	checkFile := 0
	for _, file := range files {
		// 파일명
		if file.Name() == valueString {
			checkFile = 1
			break
		}
	}
	receiveVal := ""
	if checkFile == 1 {
		f, _ := os.Open("./LogData/" + valueString)
		//f, _ := ioutil.ReadFile("./LogData/"+valueString+".txt")
		buff, _ := ioutil.ReadAll(f)
		receiveVal = string(buff)
		f.Close()
	} else {
		result, err := s.db.Exec("INSERT INTO STOCKLOG(UID,CODE,ANALYZEDATE,OPTIONCHOOSE,OPTIONCHOOSE2) values (?,?,?,?,?)", value[0], value[1], value[2], value[3],value[4])
		if err != nil {
			fmt.Print("Query Error.")
			errorHandling(&res, 500, "DB Insert Error.", err)
			return
		}
		nRow, err := result.RowsAffected()
		fmt.Println("Query Ok. Deleted Count :", nRow)
		s.aiUnit.publishQueue( value[1] + "_" + value[3]+"_"+value[4]+"_"+value[0])//+"_"+value[4]
		receiveVal = s.aiUnit.consumeQueue()
		fmt.Println("receive data: ",receiveVal)
		f, _ := os.Create("./LogData/" + valueString)
		f.Write([]byte(receiveVal))
		f.Close()
	}

	res.WriteHeader(200)
	res.Write([]byte("{\n\"stockCode\" : \"" + value[1] + "\", \n\"date\" : \"" + value[2] + "\",\n\"option\" : \"" + value[3] + "\",\n\"option2\":\""+value[4]+"\",")) //
	loadLog(receiveVal, res)

	//data := strings.Split(receiveVal, "$")
	//predictString := data[0]
	//actualString := data[1]
	//predictList := strings.Split(predictString, ",")
	//predictList = predictList[:len(predictList)-1]
	//actualList := strings.Split(actualString, ",")
	//actualList = actualList[1:len(actualList)-1]
	//predictJson, err := json.Marshal(predictList)
	//actualJson, err := json.Marshal(actualList)
	//res.Write([]byte("\n\"resultData\":"))
	//res.Write(predictJson)
	//res.Write([]byte(",\n\"realData\":"))
	//res.Write(actualJson)
	//res.Write([]byte("\n}"))

}

func (s *serverHandler) onLoadHandler(res http.ResponseWriter, req *http.Request) {
	// load user information from database

	if req.Method != "GET" {
		log.Println(req.Method)
		errorHandling(&res, 400, "OnlyForGet", errors.New("Request method is Post"))
		return
	}

	u, errors := req.URL.Query()["UID"]
	if errors != true {
		panic(errors)
	}
	//쿼리문 - request로 받음 uid 이용하여 관심 종목 db에서 꺼내오기
	//requset로 부터 uid parsing
	userId := u

	queryData2, err := s.db.Query("select UNAME, PROFILELINK from USER where UID=?", userId[0])

	var userQuery [2]sql.NullString

	if err != nil {
		errorHandling(&res, 500, "DB error user data load", err)
	}
	var userData [2]string
	for queryData2.Next() {

		err = queryData2.Scan(&userQuery[0], &userQuery[1])
		if err != nil {
			errorHandling(&res, 500, "parsing erro", err)
		}
		userData[0] = userQuery[0].String
		userData[1] = userQuery[1].String
	}

	queryData, err := s.db.Query("select CODE,INSERTDATE,REALFIRST,PREDICTPRICE from USERSTOCK where UID=?", userId[0])
	if err != nil {
		errorHandling(&res, 500, "DB error", err)
		return
	}

	var queryString [4]sql.NullString
	var stockData userStockData
	for queryData.Next() {
		err = queryData.Scan(&queryString[0], &queryString[1], &queryString[2],&queryString[3])
		if err != nil {
			errorHandling(&res, 500, "parsing erro", err)
		}
		addStock(&stockData, queryString)
	}
	res.WriteHeader(200)
	res.Write([]byte("{ \"userName\":\"" + userData[0] + "\",\n\"profileLink\":\"" + userData[1] + "\","))
	res.Write([]byte("\n\"favoriteStocks\":["))
	i := 0
	for i < len(stockData.code) {
		res.Write([]byte("{\"stockCode\":\""))
		res.Write([]byte(stockData.code[i]))
		res.Write([]byte("\",\"date\":\""))
		res.Write([]byte(stockData.date[i]))
		res.Write([]byte("\",\"realFirst\":\""))
		res.Write([]byte(stockData.price[i]))
		res.Write([]byte("\",\"predictLast\":\""))
		res.Write([]byte(stockData.predict[i]))
		res.Write([]byte("\"}"))
		i++
		if i < len(stockData.code) {
			res.Write([]byte(","))
		}
	}
	i = 0
	res.Write([]byte("],\n\"analyzedStocks\":["))

	queryData, err = s.db.Query("select CODE,ANALYZEDATE,OPTIONCHOOSE,OPTIONCHOOSE2 from STOCKLOG where UID=?", userId[0])
	if err != nil {
		errorHandling(&res, 500, "DB error", err)
		return
	}

	var queryString3 [4]sql.NullString
	var logString []string
	for queryData.Next() {
		err = queryData.Scan(&queryString3[0], &queryString3[1], &queryString3[2],&queryString3[3])
		fmt.Println(queryString3)
		if err != nil {
			errorHandling(&res, 500, "parsing erro", err)
		}
		temp:=userId[0]+"_"+queryString3[0].String+"_"+queryString3[1].String+"_"+queryString3[2].String+"_"+queryString3[3].String+".txt"
		logString=append(logString, temp)
	}
	i=0
	for _, name := range logString {
		// 파일명
		nameslice := strings.Split(name, "_")
		if nameslice[0] == userId[0] {
			temp:=strings.Split(nameslice[4],".")
			res.Write([]byte("{\"stockCode\" : \""+nameslice[1]+"\",\"date\" : \""+nameslice[2]+"\",\"option\" : \""+nameslice[3]+"\",\"option2\":\""+temp[0]+"\","))
			f, _ := os.Open("./LogData/" + name)
			buff, _ := ioutil.ReadAll(f)
			fmt.Println(name)
			receiveVal := string(buff)
			f.Close()
			loadLog(receiveVal,res)
		}
		i++
		if i!= len(logString){
			res.Write([]byte(",\n"))
		}
	}
	fmt.Println("onload success")

	res.Write([]byte(" ] }"))

}
