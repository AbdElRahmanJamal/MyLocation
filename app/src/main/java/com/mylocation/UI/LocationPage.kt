package com.mylocation.UI

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.mylocation.CoroutinseScope
import com.mylocation.LocationManagerLifeCycle
import com.mylocation.PermissionHandler
import com.mylocation.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

private const val PERMISSION_ACCESS_LOCATION = 101

class LocationPage : CoroutinseScope(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val locationPageViewModelFactory: LocationPageViewModelFactory by instance()
    private lateinit var viewModel: LocationPageViewModel
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, locationPageViewModelFactory).get(LocationPageViewModel::class.java)

        get_location.setOnClickListener {
            val permissionHandler = PermissionHandler(this)
            if (permissionHandler.hasLocationPermission()) {
                displayLocationOnScreen()
            } else {
                requestLocationPermissions()
            }

        }

    }

    private fun displayLocationOnScreen() {
        launch(Dispatchers.Main) {
            runCatching {
                viewModel.getCurrentLocation()
            }.onFailure {
                location.text = "Permission Granted"
            }.onSuccess { location.text = "latitude : ${it?.latitude} AND longitude ${it?.longitude}" }
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ACCESS_LOCATION) {
            if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                bindLocationManager()
                displayLocationOnScreen()
            } else location.text = "Permission Denied"
        }
    }

    private fun bindLocationManager() {
        LocationManagerLifeCycle(
            this,
            fusedLocationProviderClient, locationCallback
        )
    }
}
