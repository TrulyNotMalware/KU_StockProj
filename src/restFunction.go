package src

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"log"
	"net/http"
	"reflect"
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
