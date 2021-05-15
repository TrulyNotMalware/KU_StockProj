from pandas_datareader import data
import time

def makeJusikData():
    t = time.gmtime(time.time())

    jusik_code = ['005930.KS','TSLA','035420.KS','035720.KS','NEXOF']
    start_time = str(t.tm_year - 2) + '-' + str(t.tm_mon) + '-' + str(t.tm_mday)
    end_time = str(t.tm_year) + '-' + str(t.tm_mon) + '-' + str(t.tm_mday)

    for i in jusik_code:
        jusik_data = data.DataReader(i, 'yahoo', start_time, end_time)
        jusik_data['Close'].plot(figsize=(20, 10), label='Samsung')
        # plt.legend()
        # plt.show()
        jusik_data.to_csv('./data/'+i + '.csv')
    return

