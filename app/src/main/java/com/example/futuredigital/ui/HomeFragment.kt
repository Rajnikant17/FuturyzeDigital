package com.example.futuredigital.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futuredigital.adapter.HourlyTemperatureAdapter
import com.example.futuredigital.R
import com.example.futuredigital.databinding.FragmentHomeBinding
import com.example.futuredigital.util.DataState
import com.example.futuredigital.viewmodels.ActivityViewModel
import com.example.moduleapiservices.models.CustomizedClassToDisplayInAdaper
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
        // Inflate the layout for this fragment
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
        activityViewModel.LiveDataLocationPermission.observe(viewLifecycleOwner, Observer {
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
                    activityViewModel.currentTempApiSuccessfullyCalled = true
                    fragmentHomeBinding.tvErrormsg.visibility = View.GONE
                    fragmentHomeBinding.progressbarCurrent.visibility = View.GONE
                    fragmentHomeBinding.currentTemp.text =
                        String.format("%.2f", dataState.data.maindata.temp - 273)
                    fragmentHomeBinding.tvCityName.text = activityViewModel.cityFetchedFormGps
                }
                is DataState.Error -> {
                    activityViewModel.currentTempApiSuccessfullyCalled = false
                    fragmentHomeBinding.tvErrormsg.visibility = View.VISIBLE
                    fragmentHomeBinding.progressbarCurrent.visibility = View.GONE
                    fragmentHomeBinding.tvErrormsg.text = getString(R.string.currentTempErrorMsg)
                }
                is DataState.Loading -> {
                    if (!fragmentHomeBinding.progressbarCurrent.isVisible)
                        fragmentHomeBinding.progressbarCurrent.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeForecastHourly() {
        activityViewModel.getTempHourlyLiveData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<ModelForecastTemp> -> {
                    activityViewModel.hourlyForecastApiSuccessfullyCalled = true
                    fragmentHomeBinding.tvErrormsgHourlyForecast.visibility = View.GONE
                    fragmentHomeBinding.progressbarForecast.visibility = View.GONE
                    setForeCastAdapter(activityViewModel.filterOnlyTodaysForecast(dataState.data.modelForecastWeatherLists))
                }
                is DataState.Error -> {
                    activityViewModel.hourlyForecastApiSuccessfullyCalled = false
                    fragmentHomeBinding.tvErrormsgHourlyForecast.visibility = View.VISIBLE
                    fragmentHomeBinding.progressbarForecast.visibility = View.GONE
                    fragmentHomeBinding.tvErrormsgHourlyForecast.text =
                        getString(R.string.forecastErrorMsg)
                }
                is DataState.Loading -> {
                    if (!fragmentHomeBinding.progressbarForecast.isVisible)
                        fragmentHomeBinding.progressbarForecast.visibility = View.VISIBLE
                }
            }
        })
    }

    fun setForeCastAdapter(customizedClassToDisplayInAdaperList: List<CustomizedClassToDisplayInAdaper>) {
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        fragmentHomeBinding.rvHourlyforecast.setLayoutManager(linearLayoutManager)
        val hourlyTemperatureAdapter =
            HourlyTemperatureAdapter(
                requireActivity(),
                customizedClassToDisplayInAdaperList
            )
        fragmentHomeBinding.rvHourlyforecast.adapter = hourlyTemperatureAdapter
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