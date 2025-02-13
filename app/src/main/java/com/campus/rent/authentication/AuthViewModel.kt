package com.campus.rent.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campus.rent.database.model.User
import com.campus.rent.database.model.UserResponse
import com.campus.rent.database.repository.UserRepository
import com.campus.rent.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userResponseLiveData = MediatorLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = _userResponseLiveData

     fun registerUser(user: User) {
        viewModelScope.launch {
            _userResponseLiveData.postValue(NetworkResult.Loading())
            try {
                val response = userRepository.registerUser(user)
                if (response.isSuccessful) {
                    _userResponseLiveData.postValue(NetworkResult.Success(response.body()))
                } else if (response.errorBody() != null) {
                    _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))

                } else {
                    _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))

                }
            } catch (e: Exception) {
                _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))

            }

        }
    }

}