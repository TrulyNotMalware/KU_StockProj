import pika
import os
import modelControl
from makeData import makeJusikData


class connections:
    def __init__(self):
        self.url = "amqp://guest:guest@localhost:"
        self.port = "5672"
        self.filename = os.listdir('./JusikData/data')
        self.index = 1
        self.code = 'test'
        self.option = "0"
        self.chooseModel="0"
        self.uid = ""

    def receive_data(self):
        print("try connect")
        connect = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        channel = connect.channel()
        channel.queue_declare(queue='goreq')
        channel.basic_consume(self.callback,queue='goreq',no_ack=True)
        print('Wait before Enqueue.')
        channel.start_consuming()
    

    def callback(self,ch,method,properties,body):
        print("[x] Received %r" % body)
        # 0 -> 1주, 1 -> 2주, 2 -> 1달, 3 -> 3달, 4 -> 6달
        tmp=body.decode('utf-8')
        bodyArray=str(tmp).split('_')
        print(bodyArray)
        self.option=bodyArray[1]
        self.code = bodyArray[0]
        self.chooseModel=bodyArray[2]
        self.uid = bodyArray[3]
        print("do predicting...")

        # 0: RNN,
        # 1: LSTN,
        # 2: CNN,
        # 3: RNN + LSTN,
        # 4: RNN + CNN,
        # 5: LSTN + CNN,
        # 6: RNN + LSTN + CNN

        self.isSameStockData(self.code)
        loadPath = "./JusikData/data/"+self.code+".csv"
        modelControl.doTraining(loadPath,100,self.code,self.option,self.chooseModel,self.uid)
        res, actual = modelControl.predict(loadPath,self.code,self.option,self.chooseModel,self.uid)
        print("finishing Predict, Publishing.")
        self.pushingQueue(res,actual) 

    def pushingQueue(self,res,actual):
        print("try connect")
        connect = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        channel = connect.channel()
        channel.queue_declare(queue='gores')
        
        messages = ""
        for unit in res:
            strs = str(unit)
            strs = strs.replace("[","")
            strs = strs.replace("]","")
            messages += strs +","
        messages += "$,"
        for unit in actual:
            strs2 = str(unit)
            strs2 = strs2.replace("[","")
            strs2 = strs2.replace("]","")
            messages += strs2 +","
        print(messages)

        channel.basic_publish(exchange='',routing_key='gores',body=messages)
        print("[x] Sent Message.")
        self.index = self.index + 1
        
        #connect.close()
    
    def isSameStockData(self,stock_name):
        searchFileName = stock_name+ '.csv'
        for name in self.filename:
            if(name == searchFileName):
                print(stock_name+" : Founded. No have to load.")
                return
        print(stock_name+" Not Founded. Loading.")
        makeJusikData(stock_name)



if __name__ == "__main__":
    conn = connections()
    conn.receive_data()
    #for testing..
    #conn.isSameStockData('DELL')
    #doTraining("./JusikData/data/005930.KS.csv",100,1)
    #res, actual = predict("./JusikData/data/005930.KS.csv",1)
