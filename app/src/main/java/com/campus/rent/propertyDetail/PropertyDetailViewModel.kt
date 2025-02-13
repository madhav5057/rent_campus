package com.campus.rent.propertyDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campus.rent.database.model.property.getPropertyDetail.PropertyDetail
import com.campus.rent.database.repository.PropertyRepository
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(private val propertyRepository: PropertyRepository) :
    ViewModel() {


    private val _propertyResponseLiveData = MediatorLiveData<NetworkResult<PropertyDetail>>()
    val propertyResponseLiveData: LiveData<NetworkResult<PropertyDetail>> get() = _propertyResponseLiveData


    private val _property = MutableLiveData<PropertyDetail>()
    val property: LiveData<PropertyDetail> get() = _property

    fun getPropertyDetail(id: Int){

        viewModelScope.launch {

            _propertyResponseLiveData.postValue(NetworkResult.Loading())
            try {
                // Make network call to add property
                val response = propertyRepository.getProperty(id)

                if (response.isSuccessful) {
                    // Success: update LiveData with the response body
                    _propertyResponseLiveData.postValue(NetworkResult.Success(response.body()))
                    _property.value = response.body()
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