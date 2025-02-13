package com.campus.rent.database.api

import com.campus.rent.database.model.User
import com.campus.rent.database.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {


    @POST("user/registeruser.php")
    suspend fun registerUser(@Body user: User): Response<UserResponse>
}