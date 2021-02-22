package com.example.moduleapiservices.utils

import com.example.moduleapiservices.models.ModelCurrentTemp
import com.example.moduleapiservices.models.ModelForecastTemp
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/weather")
    suspend fun fetchCurrentTemperaturedata(@Query("q") city: String, @Query("APPID") id: String) : ModelCurrentTemp


    @GET("data/2.5/forecast")
    suspend fun fetchForecastTemperaturedata(@Query("q") city: String, @Query("APPID") id: String) : ModelForecastTemp
}