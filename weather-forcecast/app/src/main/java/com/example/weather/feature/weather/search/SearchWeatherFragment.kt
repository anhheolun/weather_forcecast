package com.example.weather.feature.weather.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.WeatherItem
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherSeacherBinding
import com.example.weather.delegate.viewBinding
import com.example.weather.extension.setOnDebouncedClickListener
import com.example.weather.feature.weather.search.adapter.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchWeatherFragment : Fragment(R.layout.fragment_weather_seacher) {

    private val binding: FragmentWeatherSeacherBinding by viewBinding()
    private val searchWeatherViewModel: SearchWeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        observeEvent()
    }

    private fun observeEvent() {
        searchWeatherViewModel.searchLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is SearchWeatherViewModel.SearchData.Success -> {
                    displayWeathers(it.weather.weatherItems)
                }
                is SearchWeatherViewModel.SearchData.BusinessError -> {
                    showError(it.message)
                }
                SearchWeatherViewModel.SearchData.ServerError -> {
                    showError(getString(R.string.server_error))
                }
                SearchWeatherViewModel.SearchData.CommonError -> {
                    showError(getString(R.string.common_error))
                }
                SearchWeatherViewModel.SearchData.NetworkError -> {
                    showError(getString(R.string.network_error))
                }
                SearchWeatherViewModel.SearchData.UnAuthorizedError -> {
                    showError(getString(R.string.unauthorized_error))
                }
            }
        }

        searchWeatherViewModel.searchUiStateLiveData.observe(viewLifecycleOwner) {
            binding.searchBtn.isEnabled = SearchWeatherViewModel.SearchUIState.EnableSearch == it
        }
    }

    private fun displayWeathers(weathers: List<WeatherItem>) {
        (binding.weatherRv.adapter as WeatherAdapter).apply {
            weatherItems = weathers as ArrayList<WeatherItem>
        }

    }

    private fun setUpView() {
        binding.apply {
            weatherRv.adapter = WeatherAdapter()
            weatherRv.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            searchBtn.setOnDebouncedClickListener {
                searchWeather()
            }
            searchWeatherViewModel.validateKeyword(searchEt.text?.toString() ?: "")
            searchEt.doOnTextChanged { text, _, _, _ ->
                searchWeatherViewModel.validateKeyword(text?.toString() ?: "")
            }
        }
    }

    private fun searchWeather() {
        searchWeatherViewModel.search(binding.searchEt.text.toString())
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}