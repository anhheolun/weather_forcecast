package com.example.domain.model

data class Weather(
    var code: Int,
    var message: String,
    var weatherItems: List<WeatherItem>
)
