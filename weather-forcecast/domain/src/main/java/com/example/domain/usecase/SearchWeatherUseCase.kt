package com.example.domain.usecase

import com.example.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend fun searchWeather(keyword: String) =
        weatherRepository.searchWeather(keyword = keyword,
            pageSize = PAGE_SIZE_DEFAULT,
            units = UNIT_DEFAULT,
            appId = APP_ID_DEFAULT)

    companion object {
        const val APP_ID_DEFAULT = "60c6fbeb4b93ac653c492ba806fc346d"
        const val UNIT_DEFAULT = "Metric"
        const val PAGE_SIZE_DEFAULT = 7
    }
}