from pandas_datareader import data
import time

def makeJusikData(codes):
    print("making new jusik data")
    #t = time.gmtime(time.time())

    #jusik_code = ['005930.KS','TSLA','035420.KS','035720.KS','NEXOF']
    jusik_code = []
    jusik_code.append(codes)
    #start_time = str(t.tm_year - 2) + '-' + str(t.tm_mon) + '-' + str(t.tm_mday)
    start_time = '2018-01-01'
    #end_time = str(t.tm_year) + '-' + str(t.tm_mon) + '-' + str(t.tm_mday)
    end_time = '2019-06-13'

    for i in jusik_code:
        jusik_data = data.DataReader(i, 'yahoo', start_time, end_time)
        jusik_data['Close'].plot(figsize=(20, 10), label='Samsung')
        # plt.legend()
        # plt.show()
        jusik_data.to_csv('./JusikData/data/'+i + '.csv')
        print("Writing "+str(i)+" ...")
    return

