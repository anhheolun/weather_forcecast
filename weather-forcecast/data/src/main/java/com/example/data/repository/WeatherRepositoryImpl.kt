package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.service.WeatherService
import com.example.domain.model.MyResult
import com.example.domain.model.Weather
import com.example.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
) : RemoteRepository(), WeatherRepository {
    override suspend fun searchWeather(
        keyword: String,
        pageSize: Int,
        appId: String,
        units: String,
    ): MyResult<Weather> {
        return perform(
            call = {
                weatherService.searchWeather(
                    keyword = keyword,
                    pageSize = pageSize,
                    appId = appId,
                    units = units
                )
            },
            transform = {
                it?.toDomain()
            }
        )
    }
}