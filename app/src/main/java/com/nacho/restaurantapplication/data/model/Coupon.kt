package com.nacho.restaurantapplication.data.model

data class Coupon(
    val title: String? = "",
    val description: String? = "",
    val code: String? = "",
    val expirationDate: String?,
    val amount: Int? = 0,
)