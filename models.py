import numpy as np
from tensorflow import keras
import tensorflow.keras.layers as layers



def RNN_MODEL(split_per_training):
    model = keras.Sequential()
    model.add(layers.SimpleRNN(units=100,activation='tanh',input_shape=[split_per_training,3]))
    model.add(layers.Dense(units=300,activation='relu'))
    model.add(layers.Dense(1))
    model.summary()
    model.compile(loss='mse',optimizer='adam',metrics=['mae'])
    return model


def LSTM_MODEL(split_per_training):
    model = keras.Sequential()
    model.add(layers.LSTM(units=100,activation='relu',input_shape=[split_per_training,3]))
    model.add(layers.Dense(units=512))
    #model.add(layers.Dense(units=512))
    model.add(layers.Dense(units=1))
    model.compile(loss='mse', optimizer='adam', metrics=['mae'])
    return model

def CNN_MODEL(split_per_training):
    model = keras.Sequential()
    model.add(layers.Conv1D(filters=512,kernel_size=(3,),strides=1,padding='SAME',input_shape=[split_per_training,3]))
    model.add(layers.MaxPooling1D(pool_size=2,strides=1))
    model.add(layers.BatchNormalization(axis=1))
    model.add(layers.Activation('relu'))
    model.add(layers.Conv1D(filters=256,kernel_size=(3,),strides=1,padding='SAME'))
    model.add(layers.MaxPooling1D(pool_size=2,strides=1))
    model.add(layers.BatchNormalization(axis=1))
    model.add(layers.Activation('relu'))
    model.add(layers.Conv1D(filters=128,kernel_size=(3,),strides=1,padding='SAME'))
    model.add(layers.MaxPooling1D(pool_size=2,strides=1))
    model.add(layers.BatchNormalization(axis=1))
    model.add(layers.Activation('relu'))
    model.add(layers.Flatten())
    model.add(layers.BatchNormalization(axis=1))
    model.add(layers.Activation('relu'))
    model.add(layers.Dense(units=512))
    model.add(layers.Dense(units=1))
    model.compile(loss='mse',optimizer='adam',metrics=['acc'])
    return model
