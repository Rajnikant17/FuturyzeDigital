package com.example.moduleapiservices.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelCurrentTemp (
    @SerializedName("main")
    @Expose
    var maindata: ModelCurrentMain
)