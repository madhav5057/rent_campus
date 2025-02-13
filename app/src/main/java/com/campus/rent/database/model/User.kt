package com.campus.rent.database.model

data class User(
    val user_id: String,
    val user_name: String,
    val email: String,
    val profile_img: String,
    val contact_no: String
)