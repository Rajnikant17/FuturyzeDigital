package com.example.moduleapiservices.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelCurrentMain (
    @SerializedName("temp")
    @Expose
    var temp: Double,
    @SerializedName("pressure")
    @Expose
    val pressure: Int,

    @SerializedName("humidity")
    @Expose
    val humidity: Int,

    @SerializedName("temp_min")
    @Expose
    val tempMin: Double,

    @SerializedName("temp_max")
    @Expose
    val tempMax: Double
)