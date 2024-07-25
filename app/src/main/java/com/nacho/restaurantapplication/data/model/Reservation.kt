package com.nacho.restaurantapplication.data.model

data class Reservation(
    val store: Store? = null,
    val day: String = "",
    val hour: String = "",
    val places: String
)