package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    val status: String,
    val places: List<Place>,
    val query: String)

data class Place(
    val id: String,
    @SerializedName("place_id") val placeId: String,
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String)
data class Location(
    val lat: String,
    val lng: String)