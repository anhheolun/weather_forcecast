package com.example.data.mapper

import com.example.data.model.WeatherItemModel
import com.example.data.model.WeatherResponse
import com.example.domain.model.Weather
import com.example.domain.model.WeatherItem
import java.util.Date

fun WeatherItemModel.toDomain() = WeatherItem(
    date = Date(this.date),
    humidity = this.humidity,
    pressure = this.pressure,
    averageTemp = this.temp.day,
    description = this.weather.first().description
)

fun WeatherResponse.toDomain() = Weather(
    code = this.cod,
    message = this.message,
    weatherItems = this.weatherItems.map { it.toDomain() }
)