package com.campus.rent.database.repository

import android.util.Log
import com.campus.rent.database.api.UserApi
import com.campus.rent.database.model.User
import com.campus.rent.database.model.UserResponse
import com.campus.rent.utils.Constants.TAG
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    suspend fun registerUser(user: User): Response<UserResponse>{
        val response = userApi.registerUser(user)
        Log.d(TAG, response.body().toString())
        return response

    }

}