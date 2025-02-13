package com.campus.rent.database.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.campus.rent.database.api.PropertyApi
import com.campus.rent.database.model.UserResponse
import com.campus.rent.database.model.property.PropertyRequest
import com.campus.rent.database.model.property.getPropertyDetail.PropertyDetail
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.paging.PropertyPagingSource
import retrofit2.Response
import javax.inject.Inject

class PropertyRepository @Inject constructor(private val propertyApi: PropertyApi) {

    suspend fun addProperty(propertyRequest: PropertyRequest): Response<UserResponse> {
        val response = propertyApi.addProperty(propertyRequest)
        Log.d(TAG, response.body().toString())
        return response

    }

    fun getProperty( latitude:Double,longitude:Double) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { PropertyPagingSource(propertyApi,latitude,longitude) }
    ).liveData

    suspend fun getProperty(id: Int): Response<PropertyDetail> {
        val response = propertyApi.getPropertyDetail(id)
        return response
    }


}