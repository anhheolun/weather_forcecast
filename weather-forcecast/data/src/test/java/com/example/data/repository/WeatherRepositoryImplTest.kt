package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.model.WeatherResponse
import com.example.data.service.WeatherService
import com.example.domain.model.MyResult
import com.example.domain.model.Weather
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class WeatherRepositoryImplTest {
    @MockK
    internal lateinit var weatherService: WeatherService

    private lateinit var weatherRepository: WeatherRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        weatherRepository = WeatherRepositoryImpl(weatherService)
    }

    @Test
    fun searchWeatherSuccess() {
        val weatherResponse = createWeatherResponse()
        runBlocking {
            // given
            coEvery {
                weatherService.searchWeather(any(),
                    any(),
                    any(),
                    any())
            } returns Response.success(weatherResponse)
            // when
            val result: MyResult<Weather> = weatherRepository.searchWeather("", 1, "", "")
            // then
            assertEquals(result.value(), weatherResponse.toDomain())
        }
    }

    private fun createWeatherResponse(code: Int = 200) = WeatherResponse(
        cod = code, message = "message", weatherItems = listOf()
    )
}