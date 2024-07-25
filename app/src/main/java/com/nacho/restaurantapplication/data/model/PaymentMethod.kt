package com.nacho.restaurantapplication.data.model

data class PaymentMethod(
    val type: String = "",
    val brand: String = "",
    val number: String = "",
    val bank: String = "",
    val expiration: String = "",
    val name: String = "",
    val cvv: Int = 0,
    val favorite: Boolean = false
)