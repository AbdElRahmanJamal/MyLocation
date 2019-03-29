package com.mylocation

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.mylocation.UI.LocationPageViewModelFactory
import com.mylocation.data.LocationProvider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyLocation : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyLocation))
        //get FusedLocationProviderClient
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind() from provider { LocationProvider(instance(), instance()) }
        bind() from singleton { LocationPageViewModelFactory(instance()) }

    }
}