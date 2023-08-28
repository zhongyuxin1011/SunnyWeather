package com.sunnyweather.android.extension

import android.widget.Toast
import com.sunnyweather.android.SunnyWeatherApplication

fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(SunnyWeatherApplication.context, this, duration).show()
}