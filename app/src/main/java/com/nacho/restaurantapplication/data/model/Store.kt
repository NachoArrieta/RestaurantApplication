package com.nacho.restaurantapplication.data.model

data class Store(
    val city: String = "",
    val address: String = "",
    val schedule: String = "",
    val days: String = "",
    val image: String = "",
    val phone: String = "",
    val shipmentsOnly: Boolean = false
)