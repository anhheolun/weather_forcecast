package com.example.weather.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scottyab.rootbeer.RootBeer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val rootBeer: RootBeer,
) : ViewModel() {
    private val mainStateMutableLiveData = MutableLiveData<MainState>()
    internal val mainStateLiveData = mainStateMutableLiveData as LiveData<MainState>

    fun checkRootedDevice() {
        viewModelScope.launch {
            if (rootBeer.isRooted) {
                mainStateMutableLiveData.value = MainState.RootedDeviceState
            }
        }
    }

    internal sealed class MainState {
        object RootedDeviceState : MainState()
    }
}