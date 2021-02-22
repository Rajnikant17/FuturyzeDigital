package com.example.futuredigital.ui

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.futuredigital.R
import com.example.futuredigital.viewmodels.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGpsEnabledOrNot()
        observeLocationPermissionStatus()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    fun checkGpsEnabledOrNot() {
        if (activityViewModel.isGPSEnabled()) {
            activityViewModel.checkLocationPermission()
        } else {
            Toast.makeText(activity, "Please turn on the GPS and restart the App.", Toast.LENGTH_LONG).show()
        }
    }

    fun observeLocationPermissionStatus() {
        activityViewModel.LiveDataLocationPermission.observe(this, Observer {
            if (it) {
               // get current Location
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 2000
                )
            }
        })
    }

}