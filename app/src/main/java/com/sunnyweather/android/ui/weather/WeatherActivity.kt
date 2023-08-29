package com.sunnyweather.android.ui.weather

import android.content.Context
import android.graphics.Color
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.databinding.ForecastItemBinding
import com.sunnyweather.android.extension.toast
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.ui.listener.ItemClickListener
import com.sunnyweather.android.ui.place.PlaceFragment
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    val viewModel by lazy {
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        binding.swipeRefresh.setColorSchemeResources(typedValue.resourceId)

        binding.now.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                "无法成功获取天气信息".toast()
                it.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing = false
        })
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        refreshWeather()

        val placeFragment = supportFragmentManager.findFragmentById(R.id.placeFragment) as PlaceFragment
        placeFragment.setExtItemClickListener(object : ItemClickListener<Place> {
            override fun onItemClicked(position: Int, place: Place) {
                binding.drawerLayout.closeDrawers()
                viewModel.locationLng = place.location.lng
                viewModel.locationLat = place.location.lat
                viewModel.placeName = place.name
                refreshWeather()
            }

        })
    }

    private fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // now布局
        binding.now.currentTemp.text = "${realtime.temperature.toInt()} °C"
        binding.now.currentSky.text = getSky(realtime.skycon).info
        binding.now.currentSkyIcon.setImageResource(getSky(realtime.skycon).icon)
        binding.now.currentAQI.text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()} ${realtime.airQuality.description.chn}"
        binding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // forecast布局
        binding.forecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sky = getSky(skycon.value)

            val forecastItemBinding = ForecastItemBinding.inflate(layoutInflater)
            forecastItemBinding.dateInfo.text = simpleDateFormat.format(skycon.date)
            forecastItemBinding.skyIcon.setImageResource(sky.icon)
            forecastItemBinding.skyInfo.text = sky.info
            forecastItemBinding.temperatureInfo.text = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"

            binding.forecast.forecastLayout.addView(forecastItemBinding.root)
        }
        // life_index布局
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.lifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc

        binding.weatherLayout.visibility = View.VISIBLE
    }
}