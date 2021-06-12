from tensorflow import keras
import tensorflow.keras.layers as layers
import numpy as np
import matplotlib.pyplot as plt
import models
import pandas as pd

def minMaxScaler(data):
    Det = np.max(data,axis=0)-np.min(data,axis=0)
    num = data-np.min(data,axis=0)
    return num/Det

def back_MinMax(data,value):
    diff = np.max(data,axis=0)-np.min(data,axis=0)
    back = value * diff + np.min(data,axis=0)
    return back

def makeNextData(timeSeries,seqLength):
    xdata = []
    ydata = []
    for i in range(0, len(timeSeries) - seqLength):
        tx = timeSeries[i:i + seqLength, :-1]
        print("tx :",tx)
        ty = timeSeries[i + seqLength, [-1]]
        print("ty: ",ty)
        xdata.append(tx)
        ydata.append(ty)
    return np.array(xdata), np.array(ydata)

def loadData(dataPath,split_per_training):
    data = np.genfromtxt(dataPath,delimiter=",",skip_header=1,usecols=(1,2,3,4))
    train_size = int(len(data)*0.7)

    train_set = data[0:train_size]
    test_set = data[train_size+1:]
    
    train_set = minMaxScaler(train_set)
    test_set = minMaxScaler(test_set)

    trainX, trainY = makeNextData(train_set, split_per_training)
    testX, testY = makeNextData(test_set, split_per_training)
        
    return trainX,trainY,testX,testY

def doTraining(dataPath,epoch,index,option,option2,uid):
    split_per_training = 7
    if option == '1': split_per_training = 14
    elif option == '2': split_per_training = 20
    elif option == '3': split_per_training = 40
    elif option == '4': split_per_training = 60
    
    modelList = modelSelector(option2,split_per_training)

    #Set Hyper Parameters.
    #split_per_training = 7
    lr = 0.01
    split_rate=0.7
    
    #Load Data.
    trainX,trainY,testX,testY = loadData(dataPath,split_per_training)

    #Load Models.
    Repeats = 0
    for model in modelList:
        history = model.fit(trainX,trainY,validation_split=0.2,epochs=epoch,batch_size=16)
        savePath = "saveWeights/savedWeight"+str(Repeats)+index+uid+option+option2
        saveWeight(model,savePath)
        Repeats = Repeats+1
        
    #model = models.RNN_MODEL(split_per_training)
    #history = model.fit(trainX,trainY,validation_split=0.2,epochs=epoch,batch_size=16)
    #savePath = "saveWeights/savedWeight"+str(index)
    #saveWeight(model,savePath)

def saveWeight(model,filename):
    model.save_weights(filename)

def predict(dataPath,index,option,option2,uid):
    data = np.genfromtxt(dataPath,delimiter=",",skip_header=1,usecols=(1,2,3,4))

    split_per_training = 7
    if option == '1': split_per_training = 14
    elif option == '2': split_per_training = 20
    elif option == '3': split_per_training = 60
    elif option == '4': split_per_training = 120

    modelList = modelSelector(option2,split_per_training)
    #model = models.RNN_MODEL(split_per_training)
    train_size = int(len(data)*0.7)

    trainX,trainY,testX,testY = loadData(dataPath,split_per_training)
    
    Repeats = 0
    ems = []
    for model in modelList:
        model.load_weights("saveWeights/savedWeight"+str(Repeats)+index+uid+option+option2)
        #Do Test
        res = model.evaluate(testX, testY, batch_size=16)

        xhat = testX
        yhat = model.predict(xhat)
        
        Predict_Data = back_MinMax(data[train_size-split_per_training:,[-1]],yhat)
        actual = back_MinMax(data[train_size-split_per_training:,[-1]],testY)
        
        ems.append(Predict_Data)
        Repeats = Repeats + 1
    
    #if Single Model then,
    if Repeats == 1:
        print("Single Model")
        return Predict_Data, actual
    #if not a Single Model then,
    else :
        print("Not Single Model")
        ret = softVoting(np.array(ems))
        return ret, actual
       
def softVoting(results):
    ret = []
    for units in results:
        temp = units.flatten()
        ret.append(temp)
    ret = np.array(ret)
    ret = ret.mean(axis=0)
    return ret


def modelSelector(whichModel,split_per_training):
    modelList = []
    if whichModel == "0" or whichModel == "3" or whichModel== "4" or whichModel == "6":
        modelList.append(models.RNN_MODEL(split_per_training))
    if whichModel == "1" or whichModel == "3" or whichModel == "5" or whichModel == "6":
        modelList.append(models.LSTM_MODEL(split_per_training))
    if whichModel == "2" or whichModel == "4" or whichModel == "5" or whichModel == "6":
        modelList.append(models.CNN_MODEL(split_per_training))
    return modelList

#doTraining("./JusikData/data/005930.KS.csv",100,1)
#res, actual = predict("./JusikData/data/005930.KS.csv",1)
