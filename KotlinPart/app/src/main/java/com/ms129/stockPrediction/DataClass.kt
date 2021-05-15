package com.ms129.stockPrediction

import com.google.gson.annotations.SerializedName


class DataClass(
    /**
     * {
     * "userId": 1,
     * "id": 1,
     * "title": "sunt aut facere repellat ~~~",
     * "body": "quia et suscipit\nsuscipit ~~~"
     * },
     */
    val userId: Int, val title: String, @field:SerializedName("body") val text: String
) {


}