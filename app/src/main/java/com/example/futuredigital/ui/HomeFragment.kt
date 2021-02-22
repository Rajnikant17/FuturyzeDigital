package com.example.futuredigital.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.futuredigital.R
import com.example.futuredigital.databinding.FragmentHomeBinding
import com.example.futuredigital.util.DataState
import com.example.futuredigital.viewmodels.ActivityViewModel
import com.example.moduleapiservices.models.ModelCurrentTemp
import com.example.moduleapiservices.models.ModelForecastTemp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val activityViewModel: ActivityViewModel by viewModels()
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGpsEnabledOrNot()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmen
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLocationPermissionStatus()
        observersCurrentTemp()
        observeForecastHourly()
    }

    fun checkGpsEnabledOrNot() {
        if (activityViewModel.isGPSEnabled()) {
            activityViewModel.checkLocationPermission()
        } else {
            Toast.makeText(
                activity,
                "Please turn on the GPS and restart the App.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun observeLocationPermissionStatus() {
        activityViewModel.LiveDataLocationPermission.observe(this, Observer {
            if (it) {
                // get current Location
                activityViewModel.getLastLocation()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 2000
                )
            }
        })
    }


    private fun observersCurrentTemp() {
        activityViewModel.getCurrentTempLiveData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<ModelCurrentTemp> -> {
                    fragmentHomeBinding.currentTemp.text =
                        String.format("%.2f", dataState.data.maindata.temp - 273)
                    fragmentHomeBinding.tvCityName.text = activityViewModel.cityFetchedFormGps
                    //  activityViewModel.getForeCast(ActivityViewModel.MainStateEvent.GetCurrentTempEvent)
                }
                is DataState.Error -> {
                }
                is DataState.Loading -> {
                }
            }
        })
    }

    private fun observeForecastHourly() {
        activityViewModel.getTempHourlyLiveData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<ModelForecastTemp> -> {
                }
                is DataState.Error -> {
                }
                is DataState.Loading -> {
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2000 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                    activityViewModel.getLastLocation()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.permission_string),
                        Toast.LENGTH_LONG
                    ).show()
                    finishAffinity(requireActivity())
                }
                return
            }
        }
    }

}