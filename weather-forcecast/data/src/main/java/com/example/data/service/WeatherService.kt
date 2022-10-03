package com.example.data.service

import com.example.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(DAILY_WEATHER)
    suspend fun searchWeather(
        @Query("q") keyword: String,
        @Query("cnt") pageSize: Int,
        @Query("appid") appId: String,
        @Query("units") units: String,
    ): Response<WeatherResponse>
}