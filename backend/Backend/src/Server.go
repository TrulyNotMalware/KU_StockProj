package src

import (
	"fmt"
	"log"
	"net/http"
)

func RunServer(){
	handler := newServerHandler()

	//handler test.
	http.HandleFunc("/login",handler.loginHandler)
	http.HandleFunc("/onLoad",handler.onLoadHandler)
	http.HandleFunc("/interest/add",handler.interestAddHandler)
	http.HandleFunc("/interest/delete",handler.interestDeleteHandler)
	go http.HandleFunc("/analyze",handler.analyzeHandler)
	//Run Server
	err:=http.ListenAndServe(":9090",nil)
	if err!=nil{
		log.Fatal("ListenServer: ",err)
	}else{
		fmt.Println("Server Start.")
	}
}
