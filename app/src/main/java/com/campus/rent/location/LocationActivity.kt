package com.campus.rent.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.campus.rent.databinding.ActivityLocationBinding
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.LocationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLocationBinding.inflate(layoutInflater)
    }
    private var permissionDeniedPermanently = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.button.setOnClickListener {
            if (isLocationPermissionGranted()) {
                Log.d(TAG, "isLocationPermissionGranted")
                Log.d(TAG, "${LocationHelper.getSavedLocation(this)}")
            } else if (permissionDeniedPermanently) {
                Log.d(TAG, " permissionDeniedPermanently")
                openAppSettings()

            } else {
                requestLocationPermission()
                Log.d(TAG, " requestLocationPermission")

            }
        }
    }


    private fun isLocationPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("myRequest", "permission Granted")
                getUserLocation()
            } else {
                Log.d("myRequest", "permission Not Granted")
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) && !ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    permissionDeniedPermanently = true
                    Toast.makeText(
                        this,
                        "Location Permanently denied",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }


    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                LocationHelper.saveLocation(this,location.latitude,location.longitude)

            } else {
                Log.d("myRequest", "Unable to fetch your location. Please ensure GPS is enabled.")

            }
        }.addOnFailureListener {
            Log.e("myRequest", "Error fetching location: ${it.message}")
            Toast.makeText(
                this,
                "Failed to fetch location. Please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", "com.campus.rent", null)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        getUserLocation()
    }
}