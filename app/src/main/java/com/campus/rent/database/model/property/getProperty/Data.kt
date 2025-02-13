package com.campus.rent.database.model.property.getProperty

data class Data(
    val address: String,
    val amenities: List<String>,
    val bathroom: String,
    val bedroom: String,
    val city: String,
    val country: String,
    val created_at: String,
    val description: String,
    val gender: String,
    val images: List<String>,
    val latitude: String,
    val listed_by: String,
    val longitude: String,
    val pincode: String,
    val price: String,
    val property_id: String,
    val property_type: String,
    val security_amount: String,
    val state: String,
    val title: String,
    val updated_at: String,
    val user_id: String
)