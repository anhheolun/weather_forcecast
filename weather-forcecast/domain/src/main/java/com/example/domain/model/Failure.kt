package com.example.domain.model

sealed class Failure {
    object NetworkFailure : Failure()
    object ServerFailure : Failure()
    object UnKnowFailure : Failure()
    data class BusinessFailure(val code: String, val message: String) : Failure()
    object UnAuthorizedFailure : Failure()
}
