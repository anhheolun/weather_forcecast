package com.example.data.repository

import com.example.data.model.ErrorResponse
import com.example.domain.model.Failure
import com.example.domain.model.MyResult
import com.google.gson.Gson
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class RemoteRepository {
    suspend fun <T, R> perform(
        call: suspend () -> Response<T>,
        transform: ((T?) -> R?) = { null },
        onSuccess: suspend (T?, R?) -> Unit = { _, _ -> },
    ): MyResult<R> {
        return try {
            val response = call.invoke()
            when (response.code()) {
                in SUCCESS -> {
                    MyResult.Success(
                        response.body()?.let {
                        val transformedValue = transform(it)
                        onSuccess(it, transformedValue)
                        transformedValue
                    } ?: run {
                            onSuccess(null, null)
                            null
                        }
                    )
                }
                BAD_REQUEST, NOT_FOUND  -> {
                    handleBusinessException(response)
                }
                UNAUTHORIZED -> MyResult.Error(Failure.UnAuthorizedFailure)
                else -> {
                    MyResult.Error(Failure.ServerFailure)
                }
            }
        } catch (exception: Throwable) {
            handleException(exception)
        }
    }

    private fun <T, R> handleBusinessException(response: Response<T>): MyResult<R> =
        response.errorBody()?.let {
            val gson = Gson()
            val errorRes =
                gson.fromJson(it.string(), ErrorResponse::class.java)
            errorRes?.let { error ->
                MyResult.Error(Failure.BusinessFailure(error.cod, error.message))
            }
        } ?: MyResult.Error(Failure.ServerFailure)

    private fun <R> handleException(exception: Throwable): MyResult<R> =
        when (exception) {
            is UnknownHostException,
            is SocketTimeoutException,
            -> MyResult.Error(Failure.NetworkFailure)
            else -> MyResult.Error(Failure.UnKnowFailure)
        }

    companion object {
        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404
        const val BAD_GATEWAY = 504
        val SUCCESS = 200..299
    }
}
