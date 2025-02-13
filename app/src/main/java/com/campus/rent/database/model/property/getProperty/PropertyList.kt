package com.campus.rent.database.model.property.getProperty

data class PropertyList(
    val count: Int,
    val `data`: List<Data>,
    val page: Int,
    val status: String,
    val totalCount: String,
    val totalPages: Int
)