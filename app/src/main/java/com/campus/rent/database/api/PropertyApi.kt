package com.campus.rent.database.api

import com.campus.rent.database.model.UserResponse
import com.campus.rent.database.model.property.PropertyRequest
import com.campus.rent.database.model.property.getProperty.PropertyList
import com.campus.rent.database.model.property.getPropertyDetail.PropertyDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PropertyApi {

    @POST("property/addproperty.php")
    suspend fun addProperty(@Body propertyRequest: PropertyRequest): Response<UserResponse>

    @GET("property/getallproperty.php")
    suspend fun getProperty(
        @Query("page") page: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): PropertyList

    @GET("property/getpropertydetail.php")
    suspend fun getPropertyDetail(@Query("property_id") id: Int): Response<PropertyDetail>
}