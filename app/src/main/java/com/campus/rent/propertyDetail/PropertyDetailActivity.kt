package com.campus.rent.propertyDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.campus.rent.R
import com.campus.rent.database.model.property.getPropertyDetail.PropertyDetail
import com.campus.rent.databinding.ActivityPropertyDetailBinding
import com.campus.rent.propertyDetail.adapter.PropertyImagesAdapter
import com.campus.rent.utils.NetworkResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertyDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val binding by lazy {
        ActivityPropertyDetailBinding.inflate(layoutInflater)
    }
    var isExpanded = false
    private lateinit var viewModel: PropertyDetailViewModel
    private lateinit var latLng: LatLng
    private lateinit var imageList: ArrayList<String>
    private lateinit var amenitiesList: ArrayList<String>
    var list = ArrayList<AmenitiesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PropertyDetailViewModel::class.java]

        imageList = ArrayList()
        amenitiesList = ArrayList()

        val propertyId = intent?.getIntExtra("property_id", -1) ?: -1

        if (propertyId == -1) {
            Log.e("PropertyDetailActivity", "Property ID not received")
        } else {
            viewModel.getPropertyDetail(propertyId)
        }

        setUpTv()

        setUpUi()
        setUpButton()


    }

    @SuppressLint("SetTextI18n")
    private fun setUpImagesViewPager(images: ArrayList<String>) {

        val adapter = PropertyImagesAdapter(images)
        binding.vpImage.adapter = adapter

        binding.tvImgNo.text = "1/${images.size}"

        binding.vpImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in images.indices) {
                    binding.tvImgNo.text = "${position + 1}/${images.size}"
                }
            }
        })

    }

    private fun setUpButton() {

        binding.direction.setOnClickListener {
            val latitude = latLng.latitude  // Replace with your desired latitude
            val longitude = latLng.longitude // Replace with your desired longitude
            val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps") // Opens only in Google Maps

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUpUi() {

        viewModel.propertyResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let { it1 -> setUpViews(it1) }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews(data: PropertyDetail) {

        // binding.price.text = "${data.price.toDouble().toInt()}"
        binding.gender.text = data.gender
        binding.location.text = "${data.city} ,${data.state}"
        binding.type.text = data.property_type
        binding.name.text = data.owner_name
        binding.title.text = data.title
        binding.description.text = data.description
        binding.owner.text = data.listed_by
        // binding.address.text = data.address
        imageList.addAll(data.images)
        setUpImagesViewPager(imageList)
        amenitiesList.addAll(data.amenities)
        setUpAmenities(amenitiesList)
        latLng = LatLng(data.latitude.toDouble(), data.longitude.toDouble())
        Glide.with(this).load(data.user_img).into(binding.profileImage)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    private fun setUpAmenities(amenities: ArrayList<String>) {

        amenities.forEach {
            val chipText = it

            if (!TextUtils.isEmpty(chipText)) {
                val chip = Chip(this, null, R.style.Widget_App_Chip).apply {
                    text = chipText
                    // chipCornerRadius = resources.getDimension(R.dimen.chip_corner_radius)
                    setChipBackgroundColorResource(R.color.chip_background_color)
                }
                binding.chipGroup.addView(chip)


            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUpTv() {
        binding.readMore.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                binding.description.maxLines = Int.MAX_VALUE
                binding.readMore.text = "Read Less"
            } else {
                binding.description.maxLines = 3
                binding.readMore.text = "Read More"
            }
        }


    }

    /*    private fun setUpAmenitiesRv() {

            list.add(AmenitiesModel(R.drawable.wifi, "Wifi"))
            list.add(AmenitiesModel(R.drawable.fridge, "Refrigerator"))
            list.add(AmenitiesModel(R.drawable.bed, "Bed"))
            list.add(AmenitiesModel(R.drawable.table, "Table"))
            list.add(AmenitiesModel(R.drawable.wifi, "Wifi"))
            list.add(AmenitiesModel(R.drawable.fridge, "Refrigerator"))
            list.add(AmenitiesModel(R.drawable.bed, "Bed"))
            list.add(AmenitiesModel(R.drawable.table, "Table"))


            val adapter = AmenitiesAdapter(this, list)
            binding.rvAmenity.layoutManager =
                GridLayoutManager(this, 4)
            binding.rvAmenity.adapter = adapter


        }*/


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker at the property location and move the camera
        val propertyLocation = latLng


        val customMarker = getBitmapDescriptorFromDrawable(R.drawable.location_large)

        mMap.addMarker(
            MarkerOptions()
                .position(propertyLocation)
                .icon(customMarker)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLocation, 18f))


        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false
        googleMap.uiSettings.isRotateGesturesEnabled = false
        googleMap.uiSettings.isTiltGesturesEnabled = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getBitmapDescriptorFromDrawable(drawableResId: Int): BitmapDescriptor {
        val drawable = resources.getDrawable(drawableResId, null)
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}