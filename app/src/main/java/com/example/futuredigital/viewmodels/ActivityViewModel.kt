package com.example.futuredigital.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.futuredigital.repository.ApiCallBusinessLogic
import com.example.futuredigital.util.DataState
import com.example.moduleapiservices.models.CustomizedClassToDisplayInAdaper
import com.example.moduleapiservices.models.ModelCurrentTemp
import com.example.moduleapiservices.models.ModelForecastTemp
import com.example.moduleapiservices.models.ModelForecastWeatherList
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityViewModel
@ViewModelInject
constructor(
    private val apiCallBusinessLogic: ApiCallBusinessLogic,
    private val fusedLocationClient: FusedLocationProviderClient,
    application: Application
) :
    AndroidViewModel(application) {
    var LiveDataLocationPermission: MutableLiveData<Boolean> = MutableLiveData()
    val globalContext = application
    lateinit var cityFetchedFormGps: String
    var currentTempApiSuccessfullyCalled = false
    var hourlyForecastApiSuccessfullyCalled = false
    var getCurrentTempLiveData: MutableLiveData<DataState<ModelCurrentTemp>> = MutableLiveData()
    var getTempHourlyLiveData: MutableLiveData<DataState<ModelForecastTemp>> = MutableLiveData()

    fun isGPSEnabled() =
        (globalContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                globalContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                globalContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            LiveDataLocationPermission.value = false
            return
        } else {
            LiveDataLocationPermission.value = true
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                val latitude: Double = location!!.latitude
                val longitude: Double = location.longitude
                val addressList: List<Address>
                val geocoder = Geocoder(globalContext, Locale.getDefault())
                try {
                    addressList = geocoder.getFromLocation(latitude, longitude, 1)
                    val city = addressList[0].locality
                    cityFetchedFormGps = city

                    //Current temperature api called
                    if (!currentTempApiSuccessfullyCalled)
                        getCurrentTemp(MainStateEvent.GetCurrentTempEvent)

                    //Hourly forecast api called
                    if (!hourlyForecastApiSuccessfullyCalled)
                        getForeCast(MainStateEvent.GetForecastHourlyTempEvent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
    }

    fun getCurrentTemp(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetCurrentTempEvent -> {
                    apiCallBusinessLogic.execute(cityFetchedFormGps).onEach { dataState ->
                        getCurrentTempLiveData.value = dataState
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    // Getting the forecast
    fun getForeCast(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetForecastHourlyTempEvent -> {
                    apiCallBusinessLogic.executeToGetForecastTemp(cityFetchedFormGps)
                        .onEach { dataState ->
                            getTempHourlyLiveData.value = dataState
                        }.launchIn(viewModelScope)
                }
            }
        }
    }

    // Function for filtering only today's forecast .
    fun filterOnlyTodaysForecast(modelHourlyForecastList: List<ModelForecastWeatherList>): List<CustomizedClassToDisplayInAdaper> {
        val customizedClassToDisplayInAdaperList = ArrayList<CustomizedClassToDisplayInAdaper>()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // filter todays forcast only
        val filteredForecastByTodayDate = modelHourlyForecastList.filter {
            date.equals(it.dtTxt.substring(0, 10))
        }

        filteredForecastByTodayDate.forEach {
            val customizedClassToDisplayInAdaper = CustomizedClassToDisplayInAdaper()
            customizedClassToDisplayInAdaper.temp = String.format("%.2f", it.main.temp - 273)
            customizedClassToDisplayInAdaper.humidity = it.main.humidity.toString()
            customizedClassToDisplayInAdaper.wind = it.wind.speed.toString()
            customizedClassToDisplayInAdaper.hour = it.dtTxt.substring(11, 16)

            customizedClassToDisplayInAdaperList.add(customizedClassToDisplayInAdaper)
        }
        return customizedClassToDisplayInAdaperList
    }

    sealed class MainStateEvent {
        object GetCurrentTempEvent : MainStateEvent()
        object GetForecastHourlyTempEvent : MainStateEvent()
    }
}