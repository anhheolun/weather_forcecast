package com.example.weather.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityNavHostBinding
import com.example.weather.delegate.viewBinding
import com.example.weather.feature.main.dialog.RootedDeviceDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityNavHostBinding by viewBinding()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainViewModel.checkRootedDevice()
        mainViewModel.mainStateLiveData.observe(this) {
            showRootedDeviceDialog()
        }
    }

    private fun showRootedDeviceDialog() {
        RootedDeviceDialog.show(supportFragmentManager) {
            finish()
        }
    }
}