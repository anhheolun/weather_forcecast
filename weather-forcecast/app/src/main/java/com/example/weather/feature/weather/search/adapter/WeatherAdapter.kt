package com.example.weather.feature.weather.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.WeatherItem
import com.example.weather.R
import com.example.weather.databinding.WeatherItemBinding
import com.example.weather.extension.stringFormat
import kotlin.math.roundToInt

class WeatherAdapter : RecyclerView.Adapter<WeatherItemViewHolder>() {

    var weatherItems: ArrayList<WeatherItem> = arrayListOf()
    set(value) {
        notifyDataSetChanged()
        field = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder =
        WeatherItemViewHolder(WeatherItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(weatherItems[position])
    }

    override fun getItemCount(): Int = weatherItems.size

}

class WeatherItemViewHolder(private val binding: WeatherItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(weatherItem: WeatherItem) {
        binding.apply {
            itemView.context.apply {
                dateTv.text = getString(R.string.weather_info_date, weatherItem.date.stringFormat())
                tempTv.text = getString(R.string.weather_info_average_temp, weatherItem.averageTemp.roundToInt())
                pressureTv.text = getString(R.string.weather_info_pressure, weatherItem.pressure)
                humidityTv.text = getString(R.string.weather_info_humidity, weatherItem.humidity)
                descriptionTv.text = getString(R.string.weather_info_description, weatherItem.description)
            }
        }
    }
}