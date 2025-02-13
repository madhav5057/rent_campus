package com.campus.rent.database.model.property

data class Property(
    val user_id: String,
    val title: String,
    val description: String,
    val gender: String,
    val listed_by: String,
    val price: Double,
    val security_amount: Double,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val latitude: Double,
    val longitude: Double,
    val property_type: String,
    val bedroom: Int,
    val bathroom: Int

)