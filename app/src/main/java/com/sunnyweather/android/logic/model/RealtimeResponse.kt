package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

class RealtimeResponse(val status: String,
                       val result: Result,
                       @SerializedName("api_status") val apiStatus: String,
                       @SerializedName("api_version") val apiVersion: String,
                       val lang: String,
                       @SerializedName("server_time") val serverTime: Long,
                       val timezone: String,
                       val tzshift: Long,
                       val unit: String) {

    data class Result(val realtime: Realtime,
                      val primary: Int)

    data class Realtime(@SerializedName("air_quality") val airQuality: AirQuality,
                        val skycon: String,
                        val temperature: Float,
                        val visibility: Float,
                        val status: String,
                        val pressure: Float)

    data class AirQuality(val aqi: AQI,
                          val description: Description)
    data class AQI(val chn: Float,
                   val usa: Float)

    data class Description(val chn: String,
                           val usa: String)
}