package com.example.weather.feature.weather.search

import com.example.domain.model.Failure
import com.example.domain.model.MyResult
import com.example.domain.model.Weather
import com.example.domain.model.WeatherItem
import com.example.domain.usecase.SearchWeatherUseCase
import com.example.weather.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

internal class SearchWeatherViewModelTest : BaseTest() {


    private lateinit var searchWeatherViewModel: SearchWeatherViewModel

    @MockK
    internal lateinit var searchWeatherUseCase: SearchWeatherUseCase

    @Before
    fun setUp() {
        searchWeatherViewModel =
            SearchWeatherViewModel(searchWeatherUseCase)
    }

    @Test
    fun searchWeather_NoExecute() {
        searchWeatherViewModel.search("k")
        coVerify(exactly = 0) { searchWeatherUseCase.searchWeather("k") }
    }

    @Test
    fun searchWeather_thenSendSuccess() {
        val weather = createWeather()
        coEvery {
            searchWeatherUseCase.searchWeather(any())
        } returns MyResult.Success(weather)

        searchWeatherViewModel.search("keyword")

        assertEquals(searchWeatherViewModel.searchLiveData.value,
            SearchWeatherViewModel.SearchData.Success(weather))
    }

    @Test
    fun searchWeather_thenSendNetworkError() {
        coEvery {
            searchWeatherUseCase.searchWeather(any())
        } returns MyResult.Error(Failure.NetworkFailure)

        searchWeatherViewModel.search("keyword")

        assertEquals(searchWeatherViewModel.searchLiveData.value,
            SearchWeatherViewModel.SearchData.NetworkError)
    }

    @Test
    fun searchWeather_thenSendServerError() {
        coEvery {
            searchWeatherUseCase.searchWeather(any())
        } returns MyResult.Error(Failure.ServerFailure)

        searchWeatherViewModel.search("keyword")

        assertEquals(searchWeatherViewModel.searchLiveData.value,
            SearchWeatherViewModel.SearchData.ServerError)
    }

    @Test
    fun searchWeather_thenSendBusinessError() {
        val message = "message"
        coEvery {
            searchWeatherUseCase.searchWeather(any())
        } returns MyResult.Error(Failure.BusinessFailure("200", message))

        searchWeatherViewModel.search("keyword")

        assertEquals(searchWeatherViewModel.searchLiveData.value,
            SearchWeatherViewModel.SearchData.BusinessError(message))
    }

    @Test
    fun searchWeather_thenSendCommon() {
        coEvery {
            searchWeatherUseCase.searchWeather(any())
        } returns MyResult.Error(Failure.UnKnowFailure)

        searchWeatherViewModel.search("keyword")

        assertEquals(searchWeatherViewModel.searchLiveData.value,
            SearchWeatherViewModel.SearchData.CommonError)
    }

    @Test
    fun validateKeyword() {
        searchWeatherViewModel.validateKeyword("keyword")

        assertEquals(searchWeatherViewModel.searchUiStateLiveData.value,
            SearchWeatherViewModel.SearchUIState.EnableSearch)

        searchWeatherViewModel.validateKeyword("")

        assertEquals(searchWeatherViewModel.searchUiStateLiveData.value,
            SearchWeatherViewModel.SearchUIState.DisableSearch)
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
}