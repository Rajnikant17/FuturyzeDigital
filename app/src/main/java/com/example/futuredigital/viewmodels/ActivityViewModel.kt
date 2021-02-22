package com.example.futuredigital.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.futuredigital.repository.ApiCallBusinessLogic

class ActivityViewModel
@ViewModelInject
constructor(private val apiCallBusinessLogic: ApiCallBusinessLogic, application: Application) :
    AndroidViewModel(application) {
    var LiveDataLocationPermission: MutableLiveData<Boolean> = MutableLiveData()
    val globalContext = application
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
}