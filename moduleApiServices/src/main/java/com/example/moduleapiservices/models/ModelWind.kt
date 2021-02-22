package com.example.moduleapiservices.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelWind(
    @SerializedName("speed")
    @Expose
    var speed: Double
)