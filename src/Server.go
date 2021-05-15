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
	http.HandleFunc("/test",handler.testHandler)
	http.HandleFunc("/onload",handler.onLoadHandler)

	//Run Server
	err:=http.ListenAndServe(":9090",nil)
	if err!=nil{
		log.Fatal("ListenServer: ",err)
	}else{
		fmt.Println("Server Start.")
	}
}
