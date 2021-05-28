package com.ms129.stockPrediction.recentStock

object DataUtil {
    fun getStockData(): List<Stock> {
        return listOf( Stock(0,2000),
            Stock(1,2100),
            Stock(2,1700),
            Stock(3,1740),
            Stock(4,1980),
            Stock(5,2000),
            Stock(6,2100),
            Stock(7,2400),
            Stock(8,3000),
            Stock(9,1700),
            Stock(10,1500)
//            Stock(1294),
//            Stock(3944),
//            Stock(4500),
//            Stock(6969),
//            Stock(8930),
//            Stock(9900),
//            Stock(7000),
//            Stock(8300),
//            Stock(4300),
//            Stock(2003),
//            Stock(5960),
//            Stock(3403),
//            Stock(3040),
//            Stock(5060),
//            Stock(2931),
//            Stock(3030),
//            Stock(6431),
//            Stock(3948),
//            Stock(2100),
//            Stock(2030),
//            Stock(2031),
//            Stock(2039),
//            Stock(4504),
//            Stock(4912),
//            Stock(7963),
//            Stock(3929),
//            Stock(7945),
//            Stock(9920),
//            Stock(1293),
//            Stock(2192),
//            Stock(2944),
//            Stock(1912),
//            Stock(2392),
//            Stock(1029),
//            Stock(4950),
//            Stock(2392),
//            Stock(3494),
//            Stock(3590),
//            Stock(3429),
//            Stock(2066),
//            Stock(6938),
//            Stock(7939),
//            Stock(8400),
//            Stock(700),
//            Stock(8900),
//            Stock(9100),
//            Stock(1010),
//            Stock(1200),
//            Stock(1500),
//            Stock(430),
//            Stock(1220),
//            Stock(1900),
//            Stock(2210),
//            Stock(2391),
//            Stock(2399),
//            Stock(2394),
//            Stock(3494),
//            Stock(2050),
//            Stock(2150),
//            Stock(3493),
//            Stock(2942),
//            Stock(2191),
//            Stock(3050),
//            Stock(1012),
//            Stock(2129),
//            Stock(2434),
//            Stock(2933),
//            Stock(3020),
//            Stock(3430),
//            Stock(2930),
//            Stock(12041),
//            Stock(3439),
//            Stock(2032),
//            Stock(3043),
//            Stock(1990),
//            Stock(2032),
//            Stock(1700),
//            Stock(2092),
//            Stock(2399)
        )

    }

    fun getStockRealData(): List<Stock> {
        return listOf( Stock(0,1000),
            Stock(1,2000),
            Stock(2,1500),
            Stock(3,1640),
            Stock(4,2180),
            Stock(5,2300),
            Stock(6,2400),
            Stock(7,2000),
            Stock(8,4000),
            Stock(9,1900),
            Stock(10,2500)
        )
    }

}