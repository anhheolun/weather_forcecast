package com.example.weather.feature.weather.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Failure
import com.example.domain.model.Weather
import com.example.domain.model.isSuccessful
import com.example.domain.usecase.SearchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchWeatherViewModel @Inject constructor(
    private val searchWeatherUseCase: SearchWeatherUseCase,
) : ViewModel() {
    private val searchMutableLiveData = MutableLiveData<SearchData>()
    internal val searchLiveData = searchMutableLiveData as LiveData<SearchData>
    private val searchUiStateMutableLiveData = MutableLiveData<SearchUIState>()
    internal val searchUiStateLiveData = searchUiStateMutableLiveData as LiveData<SearchUIState>

    fun search(keyword: String) {
        if (keyword.length < MIN_LENGTH) return

        viewModelScope.launch {
            val result = searchWeatherUseCase.searchWeather(keyword)
            if (result.isSuccessful) {
                searchMutableLiveData.value = SearchData.Success(result.value()!!)
            } else {
                handleFailure(result.error())
            }
        }
    }

    fun validateKeyword(keyword: String) {
        searchUiStateMutableLiveData.value =
            if (keyword.length < MIN_LENGTH) SearchUIState.DisableSearch
            else SearchUIState.EnableSearch
    }

    private fun handleFailure(failure: Failure?) {
        searchMutableLiveData.value = when (failure) {
            is Failure.BusinessFailure -> SearchData.BusinessError(failure.message)
            Failure.ServerFailure -> SearchData.ServerError
            Failure.NetworkFailure -> SearchData.NetworkError
            Failure.UnAuthorizedFailure -> SearchData.UnAuthorizedError
            else -> SearchData.CommonError
        }
    }

    internal sealed class SearchData {
        data class Success(val weather: Weather) : SearchData()
        data class BusinessError(val message: String) : SearchData()
        object NetworkError : SearchData()
        object CommonError : SearchData()
        object ServerError : SearchData()
        object UnAuthorizedError : SearchData()
    }

    internal sealed class SearchUIState {
        object EnableSearch : SearchUIState()
        object DisableSearch : SearchUIState()
    }

    companion object {
        const val MIN_LENGTH = 3
    }
}