package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.model.ErrorResponse
import com.example.data.model.WeatherResponse
import com.example.data.repository.RemoteRepository.Companion.BAD_REQUEST
import com.example.data.repository.RemoteRepository.Companion.NOT_FOUND
import com.example.data.repository.RemoteRepository.Companion.UNAUTHORIZED
import com.example.data.service.WeatherService
import com.example.domain.model.Failure
import com.example.domain.model.MyResult
import com.example.domain.model.Weather
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class RemoteRepositoryImplTest {
    @MockK
    internal lateinit var weatherService: WeatherService

    @MockK
    internal lateinit var responseBody: ResponseBody

    @MockK
    internal lateinit var mediaType: MediaType

    private lateinit var weatherRepository: WeatherRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        weatherRepository = WeatherRepositoryImpl(weatherService)
    }

    @Test
    fun testBusinessFailure() {
        val errorResponse = createErrorResponse("0111")
        runBlocking {
            // given
            mockErrorBody()
            coEvery {
                weatherService.searchWeather(any(),
                    any(),
                    any(),
                    any())
            } returns Response.error(BAD_REQUEST, responseBody)
            coEvery {
                responseBody.string()
            } returns createErrorBody(errorResponse)

            // when
            val result: MyResult<Weather> = weatherRepository.searchWeather("", 1, "", "")
            // then
            assertEquals(result.error(),
                Failure.BusinessFailure(errorResponse.cod, errorResponse.message))
        }
    }

    @Test
    fun testNotFoundFailure() {
        val errorResponse = createErrorResponse("0111")
        runBlocking {
            // given
            mockErrorBody()
            coEvery {
                weatherService.searchWeather(any(),
                    any(),
                    any(),
                    any())
            } returns Response.error(NOT_FOUND, responseBody)
            coEvery {
                responseBody.string()
            } returns createErrorBody(errorResponse)

            // when
            val result: MyResult<Weather> = weatherRepository.searchWeather("", 1, "", "")
            // then
            assertEquals(result.error(),
                Failure.BusinessFailure(errorResponse.cod, errorResponse.message))
        }
    }

    @Test
    fun test401Failure() {
        runBlocking {
            // given
            mockErrorBody()
            coEvery {
                weatherService.searchWeather(any(),
                    any(),
                    any(),
                    any())
            } returns Response.error(UNAUTHORIZED, responseBody)

            // when
            val result: MyResult<Weather> = weatherRepository.searchWeather("", 1, "", "")
            // then
            assertEquals(result.error(), Failure.UnAuthorizedFailure)
        }
    }

    @Test
    fun testSuccessNoBody() {
        runBlocking {
            // when
            val result: MyResult<Weather> = weatherRepository.perform(suspend {
                Response.success(null)
            })
            // then
            assertEquals(result.value(), null)
        }
    }

    @Test
    fun testSuccessHasBody() {
        runBlocking {
            val weatherResponse = createWeatherResponse()
            // when
            val result: MyResult<Weather> = weatherRepository.perform(suspend {
                Response.success(weatherResponse)
            }, {
                it?.toDomain()
            })
            // then
            assertEquals(result.value(), weatherResponse.toDomain())
        }
    }

    @Test
    fun testSuccessNoTransform() {
        runBlocking {
            val weatherResponse = createWeatherResponse()
            // when
            val result: MyResult<Weather> = weatherRepository.perform(suspend {
                Response.success(weatherResponse)
            })
            // then
            assertEquals(result.value(), null)
        }
    }

    @Test
    fun test500Failure() {
        runBlocking {
            // given
            mockErrorBody()
            coEvery {
                weatherService.searchWeather(any(),
                    any(),
                    any(),
                    any())
            } returns Response.error(500, responseBody)

            // when
            val result: MyResult<Weather> = weatherRepository.searchWeather("", 1, "", "")
            // then
            assertEquals(result.error(), Failure.ServerFailure)
        }
    }

    private fun mockErrorBody() {
        coEvery {
            responseBody.contentType()
        } returns mediaType
        coEvery {
            responseBody.contentLength()
        } returns 1000

    }

    private fun createWeatherResponse(code: Int = 200) = WeatherResponse(
        cod = code, message = "message", weatherItems = listOf()
    )

    private fun createErrorBody(errorResponse: ErrorResponse) = Gson().toJson(errorResponse)

    private fun createErrorResponse(code: String) =
        ErrorResponse(cod = code, message = "errorMessage")
}