package src

import (
	"fmt"
	"github.com/streadway/amqp"
)

type aiControl struct {
	name                string
	userName            string
	url                 string
	port                string
	recvQueueConnection *amqp.Connection
	recvQueueChannel    *amqp.Channel
	sendQueueConnection *amqp.Connection
	sendQueueChannel    *amqp.Channel
}

func newAIControlBlock() *aiControl {
	newStruct := &aiControl{}
	//newStruct.name = name

	newStruct.userName = "guest"
	newStruct.url = "203.252.166.247"
	newStruct.port = "5672"

	newStruct.recvQueueConnection, newStruct.recvQueueChannel = newStruct.getConnection()
	newStruct.sendQueueConnection, newStruct.sendQueueChannel = newStruct.getConnection()

	//go newStruct.consumeQueue()

	return newStruct
}

func (a *aiControl) closeConnection(){
	a.sendQueueConnection.Close()
	a.recvQueueConnection.Close()
	a.recvQueueChannel.Close()
	a.sendQueueChannel.Close()

}

func (a *aiControl) getConnection() (*amqp.Connection, *amqp.Channel) {
	ConSentence := "amqp://" + a.userName + ":" + a.userName + "@" + "localhost" + ":" + a.port + "/"
	fmt.Println(ConSentence)
	conn, err := amqp.Dial(ConSentence)
	if err != nil {
		fmt.Println("Connection Error.")
		panic("Connection Error.")
	}

	channel, err := conn.Channel()
	if err != nil {
		fmt.Println("Get Channel Failed.")
		panic("Get Channel Failed.")
	}
	fmt.Println("Successfully Connected.")
	return conn, channel
}

func (a *aiControl) publishQueue(analyzeInput string) {
	a.recvQueueConnection, a.recvQueueChannel = a.getConnection()
	a.sendQueueConnection, a.sendQueueChannel = a.getConnection()
	sq, err := a.sendQueueChannel.QueueDeclare(
		"goreq",
		false,
		false,
		false,
		false,
		nil,
	)

	if err != nil {
		fmt.Println(err)
		panic("Queue Declare Error.")
	}
	fmt.Println(sq)
	err = a.sendQueueChannel.Publish(
		"",
		"goreq",
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(analyzeInput),
		},
	)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("Succesfully Published Message to Queue")

}
func (a *aiControl) consumeQueue() string{

	rq, err := a.sendQueueChannel.QueueDeclare(
		"gores",
		false,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		fmt.Println(err)
		panic("Queue Declare Error.")
	}
	fmt.Println(rq)

	msgs, _ := a.recvQueueChannel.Consume(
		"gores",
		"",
		true,
		false,
		false,
		false,
		nil,
	)
	amqpHandler := AMQPHandler{queDelivery: msgs}
	fmt.Println("consume success")
	receiveVal := amqpHandler.getFromQueue()
	a.closeConnection()
	return receiveVal
	/*
	fmt.Println(receiveVal)
	data := strings.Split(receiveVal, "$")
	predictString := data[0]
	actualString := data[1]
	predictList := strings.Split(predictString, ",")
	predictList = predictList[:len(predictList)-1]
	actualList := strings.Split(actualString, ",")
	actualList = actualList[1:len(actualList)-1]
	predictJson, err := json.Marshal(predictList)
	actualJson, err := json.Marshal(actualList)
	res.Write([]byte("\n\"resultData\":"))
	res.Write(predictJson)
	res.Write([]byte(",\n\"realData\":"))
	res.Write(actualJson)
	res.Write([]byte("\n}"))
	*/
}
