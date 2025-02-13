package com.campus.rent.mainActivity.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.campus.rent.database.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(propertyRepository: PropertyRepository) : ViewModel() {

    private val locationLiveData = MutableLiveData<Pair<Double, Double>>()
    

    val list = locationLiveData.switchMap { (latitude, longitude) ->
        propertyRepository.getProperty(latitude, longitude).cachedIn(viewModelScope)
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        locationLiveData.value = latitude to longitude
    }

}