package com.example.futuredigital.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocationModule {
    @Singleton
    @Provides
    fun locationFusedClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }
}