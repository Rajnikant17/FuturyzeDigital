package com.example.moduleapiservices.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelForecastTemp (
    @SerializedName("cod")
    @Expose
    var cod: String,
    @SerializedName("message")
    @Expose
    val message: Int,
    @SerializedName("cnt")
    @Expose
    val cnt: Int,
    @SerializedName("list")
    @Expose
    val modelForecastWeatherLists: List<ModelForecastWeatherList>
)