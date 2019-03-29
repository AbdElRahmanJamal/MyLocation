package com.mylocation.UI

import android.location.Location
import androidx.lifecycle.ViewModel
import com.mylocation.data.LocationProvider

class LocationPageViewModel(private val locationProvider: LocationProvider) : ViewModel() {

    suspend fun getCurrentLocation(): Location? {
        return locationProvider.getCurrentLocationAsync().await()
    }
}