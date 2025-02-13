package com.campus.rent.mainActivity.fragments.publish

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campus.rent.database.model.UserResponse
import com.campus.rent.database.model.property.PropertyRequest
import com.campus.rent.database.repository.PropertyRepository
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PublishViewModel @Inject constructor(val propertyRepository: PropertyRepository) :
    ViewModel() {

    //property type fragment
    private var _propertyType = MutableLiveData("")
    val propertyType: LiveData<String> get() = _propertyType

    fun setPropertyType(property: String) {
        _propertyType.value = property
    }

    //detail fragment

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private var _desc = MutableLiveData<String>()
    val desc: LiveData<String> get() = _desc

    private var _gender = MutableLiveData<String>()
    val gender: LiveData<String> get() = _gender

    private var _noOfBedroom = MutableLiveData(0)
    val noOfBedroom: LiveData<Int> get() = _noOfBedroom


    private var _noOfBathroom = MutableLiveData(0)
    val noOfBathroom: LiveData<Int> get() = _noOfBathroom


    private var _listedBy = MutableLiveData<String>()
    val listedBy: LiveData<String> get() = _listedBy


    fun setName(newText: String) {
        _name.value = newText
    }

    fun setDesc(newText: String) {
        _desc.value = newText
    }


    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun incrementBedroom() {
        _noOfBedroom.value = _noOfBedroom.value?.plus(1)
    }

    fun incrementBathroom() {
        _noOfBathroom.value = _noOfBathroom.value?.plus(1)
    }

    fun decrementBedroom() {
        if (_noOfBedroom.value != 0) {
            _noOfBedroom.value = _noOfBedroom.value?.minus(1)
        }
    }

    fun decrementBathroom() {
        if (_noOfBathroom.value != 0) {
            _noOfBathroom.value = _noOfBathroom.value?.minus(1)

        }
    }

    fun setListedBy(listedBy: String) {
        _listedBy.postValue(listedBy)
    }

    //address fragment

    private var _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    private var _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    private var _state = MutableLiveData<String>()
    val state: LiveData<String> get() = _state

    private var _country = MutableLiveData<String>()
    val country: LiveData<String> get() = _country

    private var _pinCode = MutableLiveData<String>()
    val pinCode: LiveData<String> get() = _pinCode

    private var _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> get() = _latitude

    private var _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> get() = _longitude


    fun setAddressDetails(
        address: String,
        city: String,
        state: String,
        country: String,
        pinCode: String,
        latitude: Double,
        longitude: Double
    ) {
        _address.value = address
        _city.value = city
        _state.value = state
        _country.value = country
        _pinCode.value = pinCode
        _latitude.value = latitude
        _longitude.value=longitude
    }

    fun setAddress(address: String) {
        _address.value = address
    }

    fun setCity(city: String) {
        _city.value = city
    }

    fun setState(state: String) {
        _state.value = state
    }

    fun setCountry(country: String) {
        _country.value = country
    }

    fun setPinCode(pinCode: String) {
        _pinCode.value = pinCode
    }

    fun setLatitude(latitude: String) {
        _latitude.value = latitude.toDouble()
    }


    fun setLongitude(longitude: String) {
        _longitude.value = longitude.toDouble()
    }


    //amenities fragment
    private var _amenities = MutableLiveData<List<String>>()
    val amenities: LiveData<List<String>> get() = _amenities

    init {
        // Initialize with some default amenities if needed
        _amenities.value = listOf()
    }

    // Function to add a new amenity
    fun addAmenity(amenity: String) {
        val updatedList = _amenities.value.orEmpty().toMutableList()
        updatedList.add(amenity.uppercase())
        _amenities.value = updatedList
    }

    // Function to remove an amenity
    fun removeAmenity(amenity: String) {
        val updatedList = _amenities.value.orEmpty().toMutableList()
        updatedList.remove(amenity.uppercase())
        _amenities.value = updatedList
    }


    //photos fragment
    private val _photoList = MutableLiveData<List<Uri>>()
    val photoList: LiveData<List<Uri>> get() = _photoList

    init {
        // Initialize with some default amenities if needed
        _photoList.value = listOf()
    }
    // Add photo to the list
    fun addPhoto(uri: List<Uri>) {
        val currentList = _photoList.value.orEmpty().toMutableList()
        currentList.clear()
        currentList.addAll(uri)
        _photoList.value = currentList  // Update LiveData

    }


    //payment fragment
    private var _price = MutableLiveData<String>()
    val price: LiveData<String> get() = _price

    private var _security = MutableLiveData<String>()
    val security: LiveData<String> get() = _security

    fun setPrice(newPrice: String) {
        _price.value = newPrice
    }

    fun setSecurity(security: String) {
        _security.value = security
    }

    //adding fragment

    private val _propertyResponseLiveData = MediatorLiveData<NetworkResult<UserResponse>>()
    val propertyResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = _propertyResponseLiveData

    fun addProperty(propertyRequest: PropertyRequest) {
        viewModelScope.launch {

            _propertyResponseLiveData.postValue(NetworkResult.Loading())
            delay(2500)
            try {
                // Make network call to add property
                val response = propertyRepository.addProperty(propertyRequest)

                if (response.isSuccessful) {
                    // Success: update LiveData with the response body
                    _propertyResponseLiveData.postValue(NetworkResult.Success(response.body()))
                } else {
                    // Failed response from server, update with error message
                    val errorMessage = response.errorBody()?.string() ?: "Something went wrong"
                    _propertyResponseLiveData.postValue(NetworkResult.Error(errorMessage))
                }
            } catch (e: Exception) {
                // Log the exception for debugging purposes
                Log.e(TAG, "Error adding property: ${e.message}", e)

                // Update LiveData with a generic error message
                _propertyResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        }
    }
}