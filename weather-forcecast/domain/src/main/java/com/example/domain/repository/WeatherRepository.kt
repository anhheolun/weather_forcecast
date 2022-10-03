package com.example.domain.repository

import com.example.domain.model.MyResult
import com.example.domain.model.Weather

interface WeatherRepository {
    suspend fun searchWeather(
        keyword: String,
        pageSize: Int,
        appId: String,
        units: String,
    ) : MyResult<Weather>
}