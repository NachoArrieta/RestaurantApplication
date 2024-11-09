package com.nacho.restaurantapplication.data.model

data class Reservation(
    val reservationId: String = "",
    val city: String = "",
    val day: String = "",
    val hour: String = "",
    val places: String = ""
)