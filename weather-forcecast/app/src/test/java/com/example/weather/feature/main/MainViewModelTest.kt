package com.example.weather.feature.main

import com.example.weather.BaseTest
import com.scottyab.rootbeer.RootBeer
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class MainViewModelTest : BaseTest() {


    private lateinit var mainViewModel: MainViewModel

    @MockK
    internal lateinit var rootBeer: RootBeer

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(rootBeer)
    }

    @Test
    fun checkRootedDevice_thenSendRootedDeviceState() {
        coEvery {
            rootBeer.isRooted
        } returns true

        mainViewModel.checkRootedDevice()

        assertEquals(mainViewModel.mainStateLiveData.value,
            MainViewModel.MainState.RootedDeviceState)
    }

    @Test
    fun checkRootedDevice_thenNoSendRootedDeviceState() {
        coEvery {
            rootBeer.isRooted
        } returns false

        mainViewModel.checkRootedDevice()

        assertEquals(mainViewModel.mainStateLiveData.value, null)
    }
}