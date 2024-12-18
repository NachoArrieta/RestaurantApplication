package com.nacho.restaurantapplication.data.model

data class DeliveryMethod(
    val type: String = "",
    val shippingPrice: Int = 0,
    val address: String = "",
    val floor: String = "",
    val number: String = "",
    val city: String = ""
)



