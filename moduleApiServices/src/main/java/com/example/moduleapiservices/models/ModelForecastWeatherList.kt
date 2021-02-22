package com.example.moduleapiservices.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelForecastWeatherList (
    @SerializedName("main") @Expose
    var main: ModelForecastMain,
    @SerializedName("wind") @Expose
    var wind: ModelWind,
    @SerializedName("dt_txt")
    @Expose
    val dtTxt: String
)