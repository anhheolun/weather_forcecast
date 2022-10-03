package com.example.domain.model

import java.util.Date

data class WeatherItem(
    val date: Date,
    val averageTemp: Double,
    val pressure: Int,
    val humidity: Int,
    val description: String
)
