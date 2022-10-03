package com.example.domain.model

sealed class MyResult<out R> {
    data class Success<out T>(val data: T?) : MyResult<T>()
    data class Error(val failure: Failure) : MyResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$failure]"
        }
    }

    fun value(): R? = when (this) {
        is Error -> null
        is Success -> data
    }

    fun error(): Failure? = when (this) {
        is Error -> failure
        is Success -> null
    }
}

/**
 * `true` if [MyResult] is of type [Success]
 */
val MyResult<*>.isSuccessful
    get() = this is MyResult.Success