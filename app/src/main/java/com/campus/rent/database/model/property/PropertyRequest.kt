package com.campus.rent.database.model.property

data class PropertyRequest(
    val amenities: List<String>,
    val images: List<String>,
    val `property`: Property
)