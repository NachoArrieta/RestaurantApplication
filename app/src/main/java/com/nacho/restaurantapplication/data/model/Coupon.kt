package com.nacho.restaurantapplication.data.model

data class Coupon(
    val title: String = "",
    val description: String = "",
    val percentage: Int = 0,
    val expirationDate: String,
    val amount: String = "",
)