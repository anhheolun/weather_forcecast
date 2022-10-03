package com.example.domain.usecase

import com.example.domain.model.Failure
import com.example.domain.model.MyResult
import com.example.domain.model.Weather
import com.example.domain.model.WeatherItem
import com.example.domain.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

internal class SearchWeatherUseCaseTest {

    @MockK
    internal lateinit var weatherRepository: WeatherRepository
    private lateinit var searchWeatherUseCase: SearchWeatherUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        searchWeatherUseCase = SearchWeatherUseCase(weatherRepository)
    }

    @Test
    fun searchSuccess() {
        val weather = createWeather()
        coEvery {
            weatherRepository.searchWeather(KEYWORD, any(), any(), any())
        } returns MyResult.Success(weather)

        val result = runBlocking { searchWeatherUseCase.searchWeather(KEYWORD) }
        assertEquals(result, MyResult.Success(weather))
    }

    @Test
    fun search_returnError() {
        coEvery {
            weatherRepository.searchWeather(KEYWORD, any(), any(), any())
        } returns MyResult.Error(Failure.ServerFailure)

        val result = runBlocking { searchWeatherUseCase.searchWeather(KEYWORD) }
        assertEquals(result,
            MyResult.Error(Failure.ServerFailure))
    }

    private fun createWeather(code: Int = 200) = Weather(
        code = code,
        message = "message",
        weatherItems = listOf(
            WeatherItem(
                date = Date(),
                averageTemp = 30.0,
                pressure = 90,
                humidity = 70,
                description = "description"
            )
        )
    )

    companion object {
        const val KEYWORD = "keyword"
    }
}