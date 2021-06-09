package src

import (
	"github.com/streadway/amqp"
	"fmt"
)

type AMQPHandler struct{
	queDelivery <- chan amqp.Delivery
}

func (a *AMQPHandler) getFromQueue() string{
	fmt.Println("Consuming..")
	i:=0
	for d:= range a.queDelivery{
		fmt.Println("????")
		i++
		bytes := d.Body
		strs := string(bytes[:])
		if i%100==0{
			fmt.Println(i)
		}
		return strs
	}
	return ""
}
