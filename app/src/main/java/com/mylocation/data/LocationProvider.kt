package com.mylocation.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.mylocation.PermissionHandler
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class LocationProvider(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context
) {

    @SuppressLint("MissingPermission")
    fun getCurrentLocationAsync(): Deferred<Location?> {
        val permissionHandler = PermissionHandler(context)

        return run {
            if (permissionHandler.hasLocationPermission())
                fusedLocationProviderClient.lastLocation.asDeferred()
            else throw PermissionLocationNotGranted()
        }

    }


}

private fun <TResult> Task<TResult>.asDeferred(): Deferred<TResult?> {
    val deferred = CompletableDeferred<TResult>()
    this.addOnSuccessListener { result ->
        deferred.complete(result)
    }
    this.addOnFailureListener { exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred
}
