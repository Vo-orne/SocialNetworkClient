package com.example.myprofile.domain

sealed class ApiState {
    object Initial : ApiState()
    data class Success<T>(val data: T) : ApiState()
    data class Error(val error: String) : ApiState()
    object Loading : ApiState()
}