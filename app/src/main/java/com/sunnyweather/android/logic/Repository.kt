package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = SunnyWeatherNetwork.searchPlaces(query)
            if ("ok" == response.status) {
                Result.success(response.places)
            } else {
                Result.failure(RuntimeException("Response status is [${response.status}]."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}