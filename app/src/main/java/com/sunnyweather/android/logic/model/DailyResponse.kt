package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

class DailyResponse(val status: String,
                    val result: Result,
                    @SerializedName("api_status") val apiStatus: String,
                    @SerializedName("api_version") val apiVersion: String,
                    val lang: String,
                    @SerializedName("server_time") val serverTime: Long,
                    val timezone: String,
                    val tzshift: Long,
                    val unit: String) {

    data class Result(val daily: Daily,
                      val primary: Int)

    data class Daily(val temperature: List<Temperature>,
                     val skycon: List<Skycon>,
                     @SerializedName("life_index") val lifeIndex: LifeIndex,
                     val status: String)

    data class Temperature(val avg: Float,
                           val date: Date,
                           val max: Float,
                           val min: Float)

    data class Skycon(val value: String,
                      val date: Date)

    data class LifeIndex(val carWashing: List<LifeDesc>,
                         val coldRisk: List<LifeDesc>,
                         val dressing: List<LifeDesc>,
                         val ultraviolet: List<LifeDesc>,
                         val comfort: List<LifeDesc>)

    data class LifeDesc(val desc: String,
                        val index: Int,
                        val date: Date)
}