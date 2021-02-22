package com.example.futuredigital.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.futuredigital.repository.ApiCallBusinessLogic
import com.example.futuredigital.util.DataState
import com.example.moduleapiservices.models.ModelCurrentTemp
import com.example.moduleapiservices.models.ModelForecastTemp
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

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
    lateinit var CurrentTemp: String
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

    fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                //   Log.d("csdcsdc", location?.latitude.toString())
                val latitude: Double = location!!.latitude
                val longitude: Double = location.longitude
                val addressList: List<Address>
                val geocoder = Geocoder(globalContext, Locale.getDefault())
                try {
                    addressList = geocoder.getFromLocation(latitude, longitude, 1)
                    val city = addressList[0].locality
                    cityFetchedFormGps = city
//                    sharedViewModel.gotLocation = true

                    //Current temperature api called
                    getCurrentTemp(MainStateEvent.GetCurrentTempEvent)
                    //Hourly forecast api called
                    getForeCast(MainStateEvent.GetCurrentTempEvent)
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


    sealed class MainStateEvent {
        object GetCurrentTempEvent : MainStateEvent()
        object GetForecastHourlyTempEvent : MainStateEvent()
    }
}