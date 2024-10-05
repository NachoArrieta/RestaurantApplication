package com.nacho.restaurantapplication.data.response

sealed class FirebaseResponse {
    data object Error : FirebaseResponse()
    data class Success(val verified: Boolean) : FirebaseResponse()
}