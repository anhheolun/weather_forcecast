package com.example.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("cod") var cod: Int,
    @SerializedName("message") var message: String,
    @SerializedName("list") var weatherItems: List<WeatherItemModel> = listOf(),
)