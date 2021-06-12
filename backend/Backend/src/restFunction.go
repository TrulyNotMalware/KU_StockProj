package src

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"log"
	"net/http"
	"reflect"
	"strings"
	"fmt"
)

func errorHandling(res *http.ResponseWriter, code int, errMsg string, err error) {
	log.Println("error occured! : ", err, "\t", errMsg)
	(*res).WriteHeader(code)
	(*res).Write([]byte("{\"message\": \"" + errMsg + "\"}"))
}

func readFromRequest(res *http.ResponseWriter, req *http.Request, key []string, value *[]string) bool {

	resResult, err := ioutil.ReadAll(req.Body)
	log.Println("URL", req.URL.EscapedPath(), "\tIP : ", req.RemoteAddr, "\tINPUT : ", string(resResult))
	if string(resResult) == "" {
		errorHandling(res, 400, "empty data", errors.New("empty"))
		return false

	}
	var test map[string]interface{}
	_ = json.Unmarshal(resResult, &test)
	for index, data := range key {
		if reflect.TypeOf(test[data]) != reflect.TypeOf("str") {
			errorHandling(res, 400, "type fault", errors.New("json 값이 요청한 형식과 다름"))
			return false
		}
		(*value)[index] = test[data].(string)
		if (*value)[index] == "" {
			errorHandling(res, 402, data+"를 받아오는데 실패", err)
			return false
		}
	}
	return true

}

func loadLog(sentence string,res http.ResponseWriter){
	fmt.Println(sentence)
	data := strings.Split(sentence, "$")
	fmt.Println("data len:",len(data))
	predictString := data[0]
	actualString := data[1]
	predictList := strings.Split(predictString, ",")
	predictList = predictList[:len(predictList)-1]
	actualList := strings.Split(actualString, ",")
	actualList = actualList[1:len(actualList)-1]
	predictJson, _ := json.Marshal(predictList)
	actualJson, _ := json.Marshal(actualList)
	res.Write([]byte("\n\t\"resultData\":"))
	res.Write(predictJson)
	res.Write([]byte(",\n\t\"realData\":"))
	res.Write(actualJson)
	res.Write([]byte("\n}"))
}

