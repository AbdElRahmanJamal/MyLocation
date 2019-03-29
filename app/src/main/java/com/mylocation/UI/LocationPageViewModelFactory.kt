package com.mylocation.UI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mylocation.data.LocationProvider

class LocationPageViewModelFactory(private val locationProvider: LocationProvider) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationPageViewModel(locationProvider) as T
    }
}