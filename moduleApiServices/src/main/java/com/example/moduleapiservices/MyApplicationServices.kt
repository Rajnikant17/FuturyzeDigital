package com.example.moduleapiservices

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplicationServices : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}