package com.example.moduleapiservices.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelForecastMain (
    @SerializedName("temp")
    @Expose
    var temp: Double,
    @SerializedName("humidity")
    @Expose
    val humidity: Int
)