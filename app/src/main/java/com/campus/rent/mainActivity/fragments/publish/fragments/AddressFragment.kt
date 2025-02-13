package com.campus.rent.mainActivity.fragments.publish.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.campus.rent.R
import com.campus.rent.databinding.FragmentAddressBinding
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class AddressFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var mMapFragment: SupportMapFragment
    private var permissionDeniedPermanently = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userLatLng: LatLng


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mMapFragment.getMapAsync(this)

        setUpViews()
        setUpButton()
        getUserLocation()

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun setUpViews() {
        if (isLocationPermissionGranted()) {

            if (!::userLatLng.isInitialized) {
                binding.nextToAmenities.text = "Getting your location..."
                binding.mapView.visibility = View.GONE
                binding.animNoPermission.visibility = View.GONE
                binding.gettingLocation.visibility = View.VISIBLE
            } else {
                binding.nextToAmenities.text = "Save Address"
                binding.mapView.visibility = View.VISIBLE
                binding.animNoPermission.visibility = View.GONE
                binding.gettingLocation.visibility = View.GONE
            }


        } else {
            binding.nextToAmenities.text = "Enable Permission"
            binding.mapView.visibility = View.GONE
            binding.animNoPermission.visibility = View.VISIBLE
            binding.gettingLocation.visibility = View.GONE

        }
    }


    @SuppressLint("SetTextI18n")
    private fun setUpButton() {


        binding.nextToAmenities.setOnClickListener {
            if (isLocationPermissionGranted()) {
                Log.d("myRequest", "isLocationPermissionGranted")
                val bottomSheetFragment = AddressDetailFragment()
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    bottomSheetFragment.tag
                )
            } else if (permissionDeniedPermanently) {
                Log.d("myRequest", " permissionDeniedPermanently")
                Toast.makeText(requireActivity(), "open setting", Toast.LENGTH_SHORT).show()
                openAppSettings()

            } else {
                requestLocationPermission()
                Log.d("myRequest", " requestLocationPermission")

            }
        }
    }


    private fun isLocationPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                setUpViews()
                Log.d("myRequest", "permission Granted")
                getUserLocation()
            } else {
                Log.d("myRequest", "permission Not Granted")
                setUpViews()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) && !ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    permissionDeniedPermanently = true
                    Toast.makeText(
                        requireActivity(),
                        "Location Permanently denied",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (::userLatLng.isInitialized) {

            updateMapLocation()


        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLatLng = LatLng(location.latitude, location.longitude)
                    updateMapLocation()
                }
            }
        }
    }

    private fun updateMapLocation() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 19f))
        // setAddress(userLatLng.latitude, userLatLng.longitude)


        binding.myLocation.setOnClickListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 19f))
            // setAddress(userLatLng.latitude, userLatLng.longitude)
        }
        mMap.setOnCameraIdleListener {
            // Get the center position of the map
            val centerPosition = mMap.cameraPosition.target

            // Update the location in the TextView
            val locationName = getLocationDetail(centerPosition.latitude, centerPosition.longitude)
            binding.tvAddress.text = locationName
            //  setAddress(centerPosition.latitude, centerPosition.longitude)

        }

    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                userLatLng = LatLng(location.latitude, location.longitude)
                setUpViews()
                Log.d("myRequest", "$userLatLng")

            } else {
                Log.d("myRequest", "Unable to fetch your location. Please ensure GPS is enabled.")

            }
        }.addOnFailureListener {
            Log.e("myRequest", "Error fetching location: ${it.message}")
            Toast.makeText(
                requireActivity(),
                "Failed to fetch location. Please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun getLocationDetail(latitude: Double, longitude: Double): String {
        return try {

            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                sharedViewModel.setAddressDetails(
                    address.getAddressLine(0),
                    address.locality,
                    address.adminArea,
                    address.countryName,
                    address.postalCode,
                    address.latitude,
                    address.longitude
                )
                address.getAddressLine(0)
            } else {
                "Location not found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unable to get location name"
        }

    }
    /*

        private fun setAddress(latitude: Double, longitude: Double) {
            try {
                val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    sharedViewModel.setAddress(address.getAddressLine(0)) // Full address
                    sharedViewModel.setCity(address.locality)
                    sharedViewModel.setState(address.adminArea)
                    sharedViewModel.setCountry(address.countryName)
                    sharedViewModel.setPinCode(address.postalCode)
                    sharedViewModel.setLatitude(latitude.toString())
                    sharedViewModel.setLongitude(longitude.toString())

                } else {
                    Toast.makeText(requireActivity(), "Location not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireActivity(), "Unable to get location name", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    */


    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", "com.campus.rent", null)
        startActivity(intent)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        setUpViews()
        getUserLocation()
    }

}

