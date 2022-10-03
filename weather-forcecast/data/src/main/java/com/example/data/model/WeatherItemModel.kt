package com.example.data.model

import com.google.gson.annotations.SerializedName

data class WeatherItemModel(
    @SerializedName("dt") var date: Long,
    @SerializedName("temp") var temp: Temp,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("weather") var weather: List<WeatherDescription>
)

data class Temp(
    @SerializedName("day") var day: Double,
    @SerializedName("min") var min: Double,
    @SerializedName("max") var max: Double,
    @SerializedName("night") var night: Double,
    @SerializedName("eve") var eve: Double,
    @SerializedName("morn") var morn: Double
)

data class WeatherDescription(
    @SerializedName("id") var id: Int,
    @SerializedName("main") var main: String,
    @SerializedName("description") var description: String,
)